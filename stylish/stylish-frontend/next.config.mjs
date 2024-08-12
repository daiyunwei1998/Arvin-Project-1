/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: false,
  async rewrites() {
    return [
      {
        source: '/admin/checkout.html',
        destination: '/checkout.html',
      },
      {
        source: '/index.html',
        destination: '/',
      },
      {
        source: '/product.html',
        destination: '/product',
      },
      {
        source: '/profile.html',
        destination: '/profile',
      }
    ];
  },
	 logging: {
    fetches: {
      fullUrl: true,
    },
  },
}

export default nextConfig;
