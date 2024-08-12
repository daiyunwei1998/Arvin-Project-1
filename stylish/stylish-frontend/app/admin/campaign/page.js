import CampaignProductList from '@/app/components/CampaignProductList'
import styles from './campaign.module.css';
import React, { Fragment } from 'react'


export default function page() {
  return (
    <div className = {styles.main_frame}>
    <div className = {styles.stage}>
      <CampaignProductList></CampaignProductList>
    </div>
    </div>
  )
}
