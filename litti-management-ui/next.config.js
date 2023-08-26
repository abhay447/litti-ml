/** @type {import('next').NextConfig} */
const nextConfig = {
    experimental: {
        serverComponentsExternalPackages: ['react-bootstrap'],
    },
    output: 'standalone',
    target: 'server',
    env: {
        LIITI_MANAGEMENT_SERVER_URL: process.env.LIITI_MANAGEMENT_SERVER_URL
    }
}

module.exports = nextConfig
