import { Carousel } from 'antd';
import styles from './CampaignCarousel.module.css';


async function CampaignCarousel({campaigns}) {

  return (
    
      <Carousel 
        autoplay
        autoplaySpeed={4500}	
        dots={true}
        effect="fade"
      >
        
        {campaigns.map((campaign) => (
          <div className={styles.carousel_container}  key={campaign.product_id} style={{whiteSpace: 'pre-wrap'}}>
            <a 
              className={styles.carousel_item}
              href = {`product.html?id=${campaign.product_id}`}
              style={{
                backgroundImage: `url(${campaign.picture})`,
                backgroundPosition: 'center',
                backgroundSize: 'cover',
                backgroundRepeat: 'no-repeat',
              }}
            >
             <div className = {styles.carousel_content}>{campaign.content.replaceAll('\\n','\n')}</div>
             <div  className = {styles.carousel_story}>{campaign.story.replaceAll('\\n','\n')}</div>
            </a>
            </div>
        ))}
         
      </Carousel>
  );
}

export default CampaignCarousel;