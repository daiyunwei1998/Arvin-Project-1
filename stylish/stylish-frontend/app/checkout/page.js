import React from 'react'
import { cookies } from 'next/headers'
import Cart from './Cart';

export default function page() {
    const cookieStore = cookies()
    const jwt = cookieStore.get('jwt')
  return (
    <Cart jwt = {jwt}></Cart>
  )
}
