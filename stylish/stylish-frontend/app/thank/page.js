import styles from './Thank.module.css';
import Link from 'next/link';

function Thank({searchParams}) {
    const {order} = searchParams;
  return (
    <div className={styles.container}>
      <h1 className={styles.title}>感謝您的訂購！</h1>
      <div className={styles.message}>
        <p>您的訂單已成功送出。</p>
        <p>我們將盡快處理您的訂單並進行配送。</p>
      </div>
      <div className={styles.orderNumber}>
        <p>訂單編號：</p>
        <span>{order}</span>
      </div>
      <p className={styles.email}>訂單確認郵件已發送至您的信箱，請查收。</p>
      <Link href="/" className={styles.button}>
        返回首頁
      </Link>
    </div>
  );
}

export default Thank;