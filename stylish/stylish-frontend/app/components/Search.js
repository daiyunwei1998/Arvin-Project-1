"use client"
import React from 'react'
import styles from './Search.module.css'
import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function Search() {
    const [query, setQuery] = useState('');
    const router = useRouter();

    const handleChange = (event) => {
        setQuery(event.target.value);
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            const encodedQuery = encodeURIComponent(query);
            router.push(`/index.html?q=${encodedQuery}`);
        }
    };
  return (
    <input 
    className = {styles.header_search} 
    value={query}  
    onChange={handleChange}
    onKeyDown={handleKeyPress}
    ></input>
  )
}
