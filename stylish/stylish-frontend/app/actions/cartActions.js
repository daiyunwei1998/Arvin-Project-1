'use server'

import { cookies } from 'next/headers'

export async function getJwtToken() {
  const cookieStore = cookies()
  const jwtCookie = cookieStore.get('jwt')
  return jwtCookie ? jwtCookie.value : null
}

export async function updateCartOnServer(cartItems) {
  const jwtToken = await getJwtToken()

  if (!jwtToken) {
    return null // not logged in, don't update cart in redis
  }

  try {
    const response = await fetch('https://stylish.yunweidai.net/api/1.0/user/cart', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify(cartItems),
    })

    if (!response.ok) {
      const errorMessage = await response.text()
      throw new Error(errorMessage)
    }

    return await response.json()
  } catch (error) {
    console.error('Error updating cart:', error)
    throw error
  }
}


export async function getCartFromServer() {
  const jwtToken = await getJwtToken()

  if (!jwtToken) {
    return null // not logged in, can't fetch cart
  }

  try {
    const response = await fetch('http://localhost:8080/api/1.0/user/cart', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${jwtToken}`,
      },
    })

    if (!response.ok) {
      const errorMessage = await response.text()
      throw new Error(errorMessage)
    }

    const cartList = await response.json();

    // Transform cart items into a map
    const cartItemsMap = cartList.data.reduce((map, item) => {
        // Construct the key for each cart item
        const key = `${item.id}-${item.code}-${item.size}`;
        
        // Add item to map with the constructed key
        map[key] = item;
        
        return map;
    }, {});

    return cartItemsMap;
    
  } catch (error) {
    console.error('Error fetching cart:', error)
    throw error
  }
}