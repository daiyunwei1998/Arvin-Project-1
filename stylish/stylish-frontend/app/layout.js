import "./globals.css"
import Image from 'next/image';
import { AntdRegistry } from '@ant-design/nextjs-registry';
import styles from './layout.module.css';
import logoSrc from '../public/assets/logo.png';
import cartSrc from '../public/assets/cart-dark.png';
import memberSrc from '../public/assets/member-dark.png';
import cartSrcHover from '../public/assets/cart-hover.png';
import memberSrcHover from '../public/assets/member-hover.png';
import cartSrcMobile from '../public/assets/cart-mobile.png';
import memberSrcMobile from '../public/assets/member-mobile.png';
import Search from "./components/Search";


export const metadata = {
  title: "STYLiSH: A software engineer milestone",
  description: "course project by Yunwei (Arvin) Dai",
};

const navItems = [
  { key: '1', label: '女裝' },
  { key: '2', label: '男裝' },
  { key: '3', label: '配件' },
];


export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <AntdRegistry>
            <header className={styles.header}>
            <a href = "/">
              <Image
                src={logoSrc}
                alt="cart"
                width={258}
                height={48}
                className={styles.logo_image}
              />
            </a>
              <nav className={styles.header_categories}>
                {/* Add your navigation links here */}
                <a className = {styles.header_category} href="/index.html?category=women">女裝</a>
                <a className = {styles.header_category} href="/index.html?category=men">男裝</a>
                <a className = {styles.header_category} href="/index.html?category=accessories">配件</a>
              </nav>
              <Search/>
             <div className = {styles.header_links}>
            <div className = {styles.header_link}>
              <a href = "/checkout">
              <Image src={cartSrc} alt="member" width={44} height={44} className={styles.cart_image} />
              <Image src={cartSrcMobile} alt="member" width={44} height={44} className={styles.cart_image_mobile} />
              <Image src={cartSrcHover} alt="member" width={44} height={44} className={styles.cart_image_hover} />
              <div className = {styles.header_links_text}>購物車</div>
              </a>
            </div>
            <div className = {styles.header_link}>
              <a href = "/profile">
                <Image src={memberSrc} alt="profile" width={44} height={44} className={styles.member_image} />
                <Image src={memberSrcMobile} alt="profile" width={44} height={44} className={styles.member_image_mobile} />
                <Image src={memberSrcHover} alt="profile" width={44} height={44} className={styles.member_image_hover} />
                <div className = {styles.header_links_text}>會員</div>
              </a>
            </div>
             </div>
           </header>
            <div className = {styles.content_wrapper} >
              <div style={{ background: '#fff', minHeight: 280 }}>
              {children}
                </div>
            </div>
              
             
              
         <div className = {styles.footer}>
            <div className = {styles.footer_container}>
              <div className = {styles.footer_links}>
                <div className = {styles.footer_link}>
                  <a href = "/">關於 Stylish</a>
                </div>
                <div className = {styles.footer_link}>
                  <a href = "/">服務條款</a>
                </div>
                <div className = {styles.footer_link}>
                  <a href = "/">隱私政策</a>
                </div>
                <div className = {styles.footer_link}>
                  <a href = "/">聯絡我們</a>
                </div>
                <div className = {styles.footer_link}>
                  <a href = "/">FAQ</a>
                </div>
              </div>
              <div className = {styles.footer_social_media}>
                  <div className = {styles.footer_social_media_line}></div>
                  <div className = {styles.footer_social_media_twitter}></div>
                  <div className = {styles.footer_social_media_facebook}></div>
              </div>
              <div className = {styles.footer_copyright}>
              © {new Date().getFullYear()}. All rights reserved.
              </div>

            </div>
         </div>
           
          
        
        </AntdRegistry>
      </body>
    </html>
  );
}