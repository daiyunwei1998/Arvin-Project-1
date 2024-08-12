// app/api/logout/route.js

import { cookies } from 'next/headers';
import { NextResponse } from 'next/server';

export async function POST() {
  const cookieStore = cookies();
  cookieStore.delete('jwt'); // Delete the cookie named 'jwt'
  return NextResponse.json({ message: 'Logged out successfully' });
}

