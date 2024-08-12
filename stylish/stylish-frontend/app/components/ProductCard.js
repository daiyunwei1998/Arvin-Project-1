import React from 'react';
import PropTypes from 'prop-types';
import styles from './ProductCard.module.css'; // Correct import for CSS module

const ProductCard = ({ id, imageSrc, title, price, colors }) => {
  return (
    <a className={styles.product} href={`./product.html?id=${id}`}>
      <img src={imageSrc} alt={title} className={styles.product_image} />
      <div className={styles.product_colors}>
        {colors.map((color, index) => (
          <div
            key={index}
            className={styles.product_color}
            style={{ backgroundColor: color }} // Apply color dynamically
          />
        ))}
      </div>
      <div className={styles.product_title}>{title}</div>
      <div className={styles.product_price}>TWD.{price}</div>
    </a>
  );
};

ProductCard.propTypes = {
  id: PropTypes.string.isRequired,
  imageSrc: PropTypes.string.isRequired,
  title: PropTypes.string.isRequired,
  price: PropTypes.string.isRequired,
  colors: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default ProductCard;
