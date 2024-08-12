"use client"
import { useEffect, useState } from 'react';
import { useRouter } from "next/navigation";
import { updateCartOnServer } from '@/app/actions/cartActions';
import styles from "./Cart.module.css";


function Cart({jwt}) {
  const router = useRouter();
    const [cartItems, setCartItems] = useState({});
    const [recipient, setRecipient] = useState({
      name: '',
      phone: '',
      email: '',
      address: '',
      time: ''
  });
  const [payment, setPayment] = useState({
      cardNumber: '',
      expirationDate: '',
      ccv: ''
  });
  const [tpPay, setTpPay] = useState(null);

  useEffect(() => {
    // Check if TapPay script is already loaded
    if (window.TPDirect) {
     // setTpPay(window.TPDirect);
      return;
    }

    // Load TapPay SDK dynamically
    const script = document.createElement('script');
    script.src = "https://js.tappaysdk.com/tpdirect/v5.8.0";
    script.async = true;
    document.body.appendChild(script);

    script.onload = () => {
      // Initialize TapPay SDK
      TPDirect.setupSDK('12348', 'app_pa1pQcKoY22IlnSXq5m5WP5jFKzoRG58VEXpT7wU62ud7mMbDOGzCYIlzzLF', 'sandbox');
      setTpPay(TPDirect);

        // Initialize TapPay fields
        TPDirect.card.setup({
          fields: {
            number: {
              element: '#card-number',
              placeholder: '**** **** **** ****',
            },
            expirationDate: {
              element: '#card-expiration-date',
              placeholder: 'MM / YY',
            },
            ccv: {
              element: '#card-ccv',
              placeholder: '後三碼',
            }
          },
          styles: {
            // Style all elements
            'input': {
                'color': 'gray'
            },
            // Styling ccv field
            'input.ccv': {
                // 'font-size': '16px'
            },
            // Styling expiration-date field
            'input.expiration-date': {
                // 'font-size': '16px'
            },
            // Styling card-number field
            'input.card-number': {
                // 'font-size': '16px'
            },
            // style focus state
            ':focus': {
                // 'color': 'black'
            },
            // style valid state
            '.valid': {
                'color': 'green'
            },
            // style invalid state
            '.invalid': {
                'color': 'red'
            },
            // Media queries
            // Note that these apply to the iframe, not the root window.
            '@media screen and (max-width: 400px)': {
                'input': {
                    'color': 'orange'
                }
            }
        }
        });

    };

    return () => {
      document.body.removeChild(script);
    };
  }, []);

    useEffect(() => {
      // Retrieve and parse cart data from local storage
      const storedCartItems = localStorage.getItem('cart');

      if (storedCartItems) {
        try {
          const parsedCartItems = JSON.parse(storedCartItems);
          // unpack it, add original quantity, pack it
          const itemsWithOriginalQuantity = Object.fromEntries(
            Object.entries(parsedCartItems).map(([key, item]) => [
              key,
              {
                ...item,
                originalQuantity: item.quantity // Ensure this is set
              }
            ])
          );
          setCartItems(itemsWithOriginalQuantity);
        } catch (error) {
          console.error('Failed to parse cart items from local storage:', error);
        }
      }
    }, []);

    const handleQuantityChange = (itemId, newQuantity) => {
      // Update the quantity of the item
      setCartItems(prevItems => {
        const updatedItems = { ...prevItems };
        if (updatedItems[itemId]) {
          updatedItems[itemId].quantity = parseInt(newQuantity, 10);
          // Recalculate total
          updatedItems[itemId].total = updatedItems[itemId].quantity * updatedItems[itemId].price;
        }
        // Update local storage
        localStorage.setItem('cart', JSON.stringify(updatedItems));
        return updatedItems;
      });
    };

    const handleDeleteItem = (itemId) => {
      // Remove the item
      setCartItems(prevItems => {
        const { [itemId]: _, ...remainingItems } = prevItems;
        // Update local storage
        localStorage.setItem('cart', JSON.stringify(remainingItems));
        updateCartOnServer(Object.values(remainingItems)); // update redis
        return remainingItems;
      });
    };

    const handleFormChange = (e) => {
      const { name, value } = e.target;
      setRecipient(prev => ({ ...prev, [name]: value }));
  };

  const handleRadioChange = (e) => {
      setRecipient(prev => ({ ...prev, time: e.target.value }));
  };


  const handleSubmit = async (event) => {
    event.preventDefault(); 

      if (!jwt) {
        alert("請先登錄！");
        router.push('/profile'); 
        return;
      }
  
    if (!tpPay) {
      console.error('TapPay SDK is not loaded');
      return;
    }
  
    try {
       // Check the status of TapPay fields
       const tappayStatus = TPDirect.card.getTappayFieldsStatus();

       if (tappayStatus.canGetPrime === false) {
        alert('Payment Failure (dev: can not get prime)')
        return
    }

      
      TPDirect.card.getPrime((result) => {
        if (result.status !== 0) {
          console.error('getPrime error:', result);
          return;
        }

        const prime = result.card.prime;
        console.log('getPrime success:', prime);

        const subtotal = Object.values(cartItems).reduce((sum, item) => sum + item.total, 0);
        const freight = 30;
        const total = subtotal + freight;

        // Use the prime token to complete the payment process
        fetch('https://stylish.yunweidai.net/api/1.0/order/checkout', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            prime: prime,
            order: {
              shipping: "delivery",
              payment: "credit_card",
              subtotal: subtotal,
              freight: freight,
              total: total,
              recipient: {
                name: recipient.name,
                phone: recipient.phone,
                email: recipient.email,
                address: recipient.address,
                time: recipient.time
              },
              list: Object.values(cartItems).map(item => ({
                id: item.id,
                name: item.name,
                price: item.price,
                color: {
                  name: item.color,
                  code: item.code.replace(/^#/, '')
                },
                size: item.size,
                qty: item.quantity
              }))
            }
          }),
        }
      ).then(response => {
          if (response.ok) {
            // Response is 200 OK, consider it a success
            return response.json(); // Parse the JSON body if needed
          } else {
            throw new Error('Network response was not ok.'); 
          }
        })
        .then(result => {
          console.log('Payment successful:', result); 
           // Clear the cart from local storage
         localStorage.removeItem('cart');
    
         // Reset the cart state
         setCartItems({});
        
          router.push(`/thank?order=${result.data.number}`);
        })
        .catch(error => {
          console.error('Payment error:', error); // Handle network or other errors
        });
      });
    } catch (error) {
      console.error('Payment error:', error); 
    }
  };

    return (
      <div className={styles.cart}>
        <div className={styles.cart_header}>
          <div className={styles.cart_header_number}>購物車</div>
          <div className={styles.cart_header_quantity}>數量</div>
          <div className={styles.cart_header_price}>單價</div>
          <div className={styles.cart_header_subtotal}>小計</div>
          <div className={styles.cart_header_delete_button}></div>
        </div>

        <div className={styles.cart_items}>
          {Object.entries(cartItems).map(([key, item]) => (
            <div key={key} className={styles.cart_item}>
              <img
                src={item.picture}
                className={styles.cart_item_image}
                alt={item.name}
              />

              <div className={styles.cart_item_detail}>
                <div className={styles.cart_item_name}>{item.name}</div>
                <div className={styles.cart_item_id}>{item.id}</div>
                <div className={styles.cart_item_color}>顏色｜  {item.color}</div>
                <div className={styles.cart_item_size}>尺寸｜  {item.size}</div>
              </div>

              <div className={styles.cart_item_quantity}>
                <span className={styles.cart_item_quantity_title}>Quantity:</span>
                <select
                  className={styles.cart_item_quantity_selector}
                  value={item.quantity}
                  onChange={(e) => handleQuantityChange(key, e.target.value)}
                >
                  {Array.from({ length: item.originalQuantity }, (_, i) => i + 1).map(num => (
                    <option key={num} value={num}>{num}</option>
                  ))}
                </select>
              </div>

              <div className={styles.cart_item_price}>
                <span className={styles.cart_item_price_title}>Price:</span>$
                {item.price}
              </div>

              <div className={styles.cart_item_subtotal}>
                <span className={styles.cart_item_subtotal_title}>
                  Subtotal:
                </span>
                ${item.total}
              </div>

              <div className={styles.cart_delete_button} onClick = {() => handleDeleteItem(key)}></div>
            </div>
          ))}
        </div>
        <div className={styles.shipment}>
          <div className={styles.shipment_item_name}>配送國家</div>
          <select className={styles.shipment_item_selector}>
          <option key={"tw"} value={"tw"}>臺灣及離島</option>
          </select>
          <div className={styles.shipment_item_name}>付款方式</div>
          <select className={styles.shipment_item_selector}>
          <option key={"tw"} value={"tw"}>信用卡付款</option>
          </select>
        </div>

        <div className={styles.note}>
          {"※ 提醒您："}
          <br/>
          {"● 選擇宅配-請填寫正確收件人資訊，避免包裹配送不達"}
          <br/>
          {"● 選擇超商-請填寫正確收件人姓名(與證件相符)，避免無法領取"}
        </div>
       
        <form className={styles.form} onSubmit={handleSubmit}>
  <div className={styles.form_title}>訂購資料</div>
  
  <div className={styles.form_field}>
    <label className={styles.form_field_name}>收件人姓名</label>
    <input type="text"  name="name" className={styles.form_field_input} value={recipient.name} onChange={handleFormChange} />
  </div>
  
  <div className={styles.form_note}>
    務必填寫完整收件人姓名，避免包裹無法順利簽收
  </div>
  
  <div className={styles.form_field}>
    <label className={styles.form_field_name}>Email</label>
    <input type="email" name="email" className={styles.form_field_input} value = {recipient.email} onChange={handleFormChange} />
  </div>
  
  <div className={styles.form_field}>
    <label className={styles.form_field_name}>手機</label>
    <input type="tel" name="phone" className={styles.form_field_input} value={recipient.phone} onChange={handleFormChange} />
  </div>
  
  <div className={styles.form_field}>
    <label className={styles.form_field_name}>地址</label>
    <input type="text" name="address"  className={styles.form_field_input}   value={recipient.address} onChange={handleFormChange} />
  </div>
  
  <div className={styles.form_field}>
    <div className={styles.form_field_name}>配送時間</div>
    <div className={styles.form_field_radios}>
      <label className={styles.form_field_radio}>
        <input type="radio" name="time" value = "morning"  checked={recipient.time === 'morning'} onChange={handleRadioChange}/> 08:00-12:00
      </label>
      <label className={styles.form_field_radio}>
        <input type="radio" name="time" value = "afternoon" checked={recipient.time === 'afternoon'} onChange={handleRadioChange} /> 14:00-18:00
      </label>
      <label className={styles.form_field_radio}>
        <input type="radio" name="time" value = "anytime" checked={recipient.time === 'anytime'} onChange={handleRadioChange} /> 不指定
      </label>
    </div>
  </div>

  <div className = {styles.form}>
  <div className={styles.form_title}>付款資料</div>

  <div className={styles.form_field}>
    <div className={styles.form_field_name}>信用卡號碼</div>
    <div className={styles.form_field_input} id="card-number">
    </div>
  </div>

  <div className={styles.form_field}>
    <div className={styles.form_field_name}>有效期限</div>
    <div className={styles.form_field_input} id="card-expiration-date"> 
    </div>
  </div>

  <div className={styles.form_field}>
    <div className={styles.form_field_name}>安全碼</div>
    <div className={styles.form_field_input} id="card-ccv">
    </div>
  </div>
  </div>

  <div className={styles.total}>
          <div className={styles.total_name}>總金額</div>
          <div className={styles.total_nt}>NT.</div>
          <div className={styles.total_amount}>
            ${Object.values(cartItems).reduce((sum, item) => sum + item.total, 0)}
          </div>
        </div>
        <div className={styles.freight}>
          <div className={styles.freight_name}>運費</div>
          <div className={styles.total_nt}>NT.</div>
          <div className={styles.total_amount}>
          $30
          </div>
        </div>
        <div className={styles.payable}>
          <div className={styles.payable_name}>應付金額</div>
          <div className={styles.total_nt}>NT.</div>
          <div className={styles.total_amount}>
          ${Object.values(cartItems).reduce((sum, item) => sum + item.total, 0)+30}
          </div>
        </div>
        <button  type="submit" className={styles.checkout_button}>確認付款</button>
</form>
       
      </div>

    );
}

export default Cart;
