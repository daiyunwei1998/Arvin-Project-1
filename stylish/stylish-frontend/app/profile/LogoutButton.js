// app/profile/LogoutButton.js
'use client';
import styles from './LogoutButton.module.css'

import { useRouter } from 'next/navigation';

export default function LogoutButton() {
  const router = useRouter();

  const handleLogout = async () => {
    try {
      const response = await fetch('/logout', {
        method: 'POST',
      });
      if (response.ok) {
        router.push('/');
      } else {
        console.error('Logout failed');
      }
    } catch (error) {
      console.error('Error during logout:', error);
    }
  };

  return (
    <a onClick={handleLogout} className={styles.button}>
      登出
    </a>
  );
}
