import React from "react";
import styles from "./product.module.css";
import ProductSelector from "../components/ProductSelector";

async function getProductsById(id) {
  const response = await fetch(`https://stylish.yunweidai.net/api/1.0/products/details?id=${id}`, { next: { revalidate: 60 } });
  if (!response.ok) {
    if (response.status === 404) {
      notFound();
    }
    throw new Error('You\'re not supposed to be here');
  }
  const data = await response.json();
  return data.data;
}

export default async function page({searchParams}) {
  const {id} = searchParams;
  let data = null;

  try {
    if (id) {
      data = await getProductsById(id);
      
      if (!data) {
        return <div>No product found with the given ID.</div>;
      }
    } else {
      return <div>No product ID provided.</div>;
    }
  } catch (err) {
    return <div>Error: {err.message}</div>;
  }


  return (
    <div className={styles.product}>
      <img className={styles.main_image} src={data.main_image}></img>
      <div className={styles.product_detail}>
        <div className={styles.product_title}>{data.title}</div>
        <div className={styles.product_id}>{data.id}</div>
        <div className={styles.product_price}>TWD.{data.price}</div>
        <ProductSelector
          data = {data}
        />
        <div className={styles.product_note}>{data.note}</div>
        <div className={styles.product_texture}>{data.texture}</div>
        <div className={styles.product_description}>
          {data.description.replaceAll("\\r\\n", "\n").replaceAll("\\n", "\n")}
        </div>
        <div className={styles.product_place}>素材產地 / {data.place}</div>
        <div className={styles.product_place}>加工產地 / {data.place}</div>
      </div>
      <div className={styles.product_story}>
        <div className={styles.product_story_title}>細部說明</div>
        <div className={styles.product_story_content}>{data.story}</div>
      </div>

      <div className={styles.product_images}>
        {data.images
          .filter((image) => image !== data.main_image)
          .map((image, index) => (
            <img
              key={index}
              src={image}
              className={styles.product_image}
              alt={`Product image ${index}`}
            />
          ))}
      </div>
    </div>
  );
}
