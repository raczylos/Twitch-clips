import { useState } from "react";
import React from 'react';

type Props = {
  src: string;
  alt: string;
  className?: string;
  placeholderSrc?: string;
};

const LazyImage = ({ src, alt, className = "", placeholderSrc }: Props) => {
  const [loaded, setLoaded] = useState(false);

  return (
    
    <div className={`relative overflow-hidden ${className}`}>
        
      {!loaded && (
        <img
          src={placeholderSrc || src}
          alt="Loading placeholder"
          className="absolute top-0 left-0 w-full h-full blur-md"
        />
      )}


      <img
        src={src}
        alt={alt}
        className={`transition-opacity duration-500 ease-in-out ${loaded ? "opacity-100" : "opacity-0"}`}
        onLoad={() => setLoaded(true)}
      />
    </div>
  );
};

export default LazyImage;

