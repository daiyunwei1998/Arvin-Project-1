import styles from "./page.module.css";
import { notFound } from 'next/navigation';
import ProductCard from "./components/ProductCard";
import CampaignCarousel from './components/CampaignCarousel';

export async function getCampaigns() {
  const response = await fetch('https://stylish.yunweidai.net/api/1.0/marketing/campaigns');
  if (!response.ok) {
    throw new Error('Failed to fetch campaigns');
  }
  const data = await response.json();
  return data.data;
}

async function getProductsByCategory(category) {
  const response = await fetch(`https://stylish.yunweidai.net/api/1.0/products/${category}`, { next: { revalidate: 60 } });
  if (!response.ok) {
    if (response.status === 404) {
      notFound();
    }
    throw new Error('Failed to fetch products');
  }
  const data = await response.json();
  return data.data;
}

async function getProductsByKeyword(keyword) {
  const response = await fetch(`https://stylish.yunweidai.net/api/1.0/products/search?keyword=${keyword}`, { next: { revalidate: 60 } });
  if (!response.ok) {
    if (response.status === 404) {
      notFound();
    }
    throw new Error('Failed to fetch products');
  }
  const data = await response.json();
  return data.data;
}

export default async function Home({ searchParams }) {
  const { category = 'all', q } = searchParams;
  const campaigns = await getCampaigns();

  let products = [];
  let error = null;

  try {
    if (q) {
      products = await getProductsByKeyword(q);
      if (products.length === 0) {
        return <div>No products found with the keyword.</div>;
      }
    } else {
      products = await getProductsByCategory(category);
      if (products.length === 0) {
        return <div>No products found in this category.</div>;
      }
    }
  } catch (err) {
    error = err.message;
  }

  return (
    <div>
      <CampaignCarousel campaigns={campaigns} />

      <div className={styles.products}>
        {error && <div>Error: {error}</div>}
        {products.map((product) => (
          <div key={product.id} className={styles.product}>
            <ProductCard
              id={product.id}
              imageSrc={product.main_image}
              title={product.title}
              price={product.price}
              colors={product.colors.map(color => `#${color.code.toUpperCase()}`)}
            />
          </div>
        ))}
      </div>
    </div>
  );
}
