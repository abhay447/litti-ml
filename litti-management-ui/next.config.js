/** @type {import('next').NextConfig} */

const nextConfig = {
    experimental: {
        serverComponentsExternalPackages: ['react-bootstrap'],
    },
    output: 'standalone',
    target: 'server',
    // async rewrites() {
    //     return [
    //       {
    //         source: '/backend/:path*',
    //         destination: clients_data.LIITI_MANAGEMENT_SERVER_URL+'/:path*' // Proxy to Backend
    //       }
    //     ]
    // }
}

module.exports = nextConfig
