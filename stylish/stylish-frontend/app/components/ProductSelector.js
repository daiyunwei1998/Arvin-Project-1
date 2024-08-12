"use client";
import React, { useState, useEffect } from 'react';
import { updateCartOnServer } from '@/app/actions/cartActions';
import styles from "./ProductSelector.module.css";

export default function ProductSelector({ data: { colors, sizes, variants, id, price, main_image, title } }) {
    const [selectedSize, setSelectedSize] = useState(null);
    const [selectedColor, setSelectedColor] = useState(colors.length > 0 ? `#${colors[0].code.toUpperCase()}` : null);
    const [quantity, setQuantity] = useState(1);
    const [variantStocks, setVariantStocks] = useState({});
    const [selectedColorName, setSelectedColorName] = useState("");

    useEffect(() => {
        const updateStocks = () => {
            if (typeof window !== 'undefined') {
                const cart = JSON.parse(window.localStorage.getItem('cart')) || {};
                const newStocks = {};

                variants.forEach((variant) => {
                    const colorCode = `#${variant.color_code.toUpperCase()}`;
                    const productKey = `${id}-${colorCode}-${variant.size}`;
                    const cartItem = cart[productKey];
                    const cartQuantity = cartItem ? cartItem.quantity : 0;
                    newStocks[productKey] = Math.max(0, variant.stock - cartQuantity);
                });

                setVariantStocks(newStocks);
            }
        };

        updateStocks();
    }, [variants, id]);

    useEffect(() => {
        if (selectedColor) {
            const selectedColorObject = colors.find(
                (color) => `#${color.code.toUpperCase()}` === selectedColor
            );
            setSelectedColorName(selectedColorObject ? selectedColorObject.name : "");
        }
    }, [selectedColor, colors]);

    const getStock = (color, size) => {
        const productKey = `${id}-${color}-${size}`;
        return variantStocks[productKey] || 0;
    };

    const hasStock = (size) => {
        return getStock(selectedColor, size) > 0;
    };

    const handleQuantityChange = (amount) => {
        setQuantity((prevQuantity) => {
            const currentStock = getStock(selectedColor, selectedSize);
            const newQuantity = prevQuantity + amount;
            return Math.max(1, Math.min(newQuantity, currentStock));
        });
    };

    const isSelected = (size) => selectedSize === size;


    const handleAddToCart = () => {
        if (typeof window !== 'undefined' && selectedColor && selectedSize) {
            const cart = JSON.parse(window.localStorage.getItem('cart')) || {};
            const productKey = `${id}-${selectedColor}-${selectedSize}`;
            const currentStock = getStock(selectedColor, selectedSize);

            if (quantity > currentStock) {
                alert('商品缺貨');
                return;
            }

            if (cart[productKey]) {
                cart[productKey].quantity += quantity;
                cart[productKey].total = cart[productKey].price * cart[productKey].quantity;
            } else {
                cart[productKey] = {
                    id,
                    color: selectedColorName,
                    size: selectedSize,
                    quantity,
                    picture: main_image,
                    price,
                    name: title,
                    total: price * quantity,
                    code: selectedColor
                };
            }

            window.localStorage.setItem('cart', JSON.stringify(cart));
            setVariantStocks(prevStocks => ({
                ...prevStocks,
                [productKey]: currentStock - quantity
            }));
            setQuantity(1);
            console.log('Added to cart:', cart[productKey]);

            updateCartOnServer(Object.values(cart));
        
        }
    };

    return (
        <>
            <div className={styles.product_variant}>
                <div className={styles.product_color_title}>顏色｜</div>
                <div className={styles.product_color_selector}>
                    {colors.map((color) => (
                        <div
                            key={color.code}
                            className={`${styles.product_color} ${
                                selectedColor === `#${color.code.toUpperCase()}`
                                    ? styles.product_color_selected
                                    : ""
                            }`}
                            style={{ backgroundColor: `#${color.code.toUpperCase()}` }}
                            onClick={() => {
                                setSelectedColor(`#${color.code.toUpperCase()}`);
                                setSelectedSize(null);
                                setQuantity(1);
                            }}
                        />
                    ))}
                </div>
            </div>
            <div className={styles.product_variant}>
                <div className={styles.product_size_title}>尺寸｜</div>
                <div className={styles.product_size_selector}>
                    {sizes.map((size) => (
                        <div
                            key={size}
                            className={`${styles.product_size} ${
                                isSelected(size)
                                    ? styles.product_size_selected
                                    : !hasStock(size)
                                    ? styles.product_size_disabled
                                    : ""
                            }`}
                            onClick={() => {  
                                if (hasStock(size)) {
                                    setSelectedSize(size);
                                    setQuantity(1);
                                } 
                            }}
                        >
                            {size}
                        </div>
                    ))}
                </div>
            </div>
            <div className={styles.product_variant}>
                <div className={styles.product_quantity_title}>數量｜</div>
                <div className={styles.product_quantity_selector}>
                    <div className={styles.product_quantity_minus} onClick={() => handleQuantityChange(-1)}></div>
                    <div className={styles.product_quantity_value}>{quantity}</div>
                    <div className={styles.product_quantity_add} onClick={() => handleQuantityChange(1)}></div>
                </div>
            </div>
            <button 
                className={styles.product_add_to_cart_button} 
                onClick={selectedColor && selectedSize ? handleAddToCart : null}
                disabled={!selectedColor || !selectedSize || getStock(selectedColor, selectedSize) === 0}
            >
                {!selectedColor || !selectedSize ? '請選擇尺寸和顏色' : 
                 getStock(selectedColor, selectedSize) === 0 ? '缺貨中' : '加入購物車'}
            </button>
        </>
    );
}