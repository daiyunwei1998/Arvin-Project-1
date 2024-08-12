"use client"
import { useEffect } from 'react';
import { getCartFromServer, updateCartOnServer } from '@/app/actions/cartActions'; 

export default function LocalStorageUpdater({ jwt }) {
  useEffect(() => {
    const fetchDataAndUpdateLocalStorage = async () => {
      try {
        // Fetch cart data from the server using jwt
        const cartFromServer = await getCartFromServer(jwt);

        // Load existing cart data from localStorage
        const cartFromLocalStorage = JSON.parse(localStorage.getItem('cart')) || {};

        // Merge fetched cart data with existing cart data in localStorage
        for (const key in cartFromServer) {
          if (!cartFromLocalStorage[key]) {
            cartFromLocalStorage[key] = cartFromServer[key];
          }
        }

        // Save updated cart data back to localStorage
        localStorage.setItem('cart', JSON.stringify(cartFromLocalStorage));

         // update redis
            updateCartOnServer( Object.values(cartFromLocalStorage)); // TODO: can modify function signiture

        console.log('Merged cart:', cartFromLocalStorage);
      } catch (error) {
        console.error('Error fetching or updating data:', error);
      }
    };

    // Call the function to fetch and update data when jwt changes
    fetchDataAndUpdateLocalStorage();
  }, [jwt]); // Depend on jwt to trigger updates when it changes


  return null; // This component doesn't render anything in the UI
}
