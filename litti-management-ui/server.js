const http = require('http')
const { parse } = require('url')
const next = require('next')
// const dotenv = require('dotenv');

// dotenv.config()
const dev = process.env.NODE_ENV !== 'production'
const hostname = process.env["HOSTNAME"].toString()
const port =  process.env["PORT"].toString()
// when using middleware `hostname` and `port` must be provided below
const app = next({ dev, hostname, port })
const handle = app.getRequestHandler()

 
app.prepare().then(() => {
  http.createServer(async (req, res) => {
    try {
      const managementServerHost = process.env["LITTI_MANAGEMENT_SERVER_HOST"].toString()
      const managementServerPort = process.env["LITTI_MANAGEMENT_SERVER_PORT"].toString()
      // Be sure to pass `true` as the second argument to `url.parse`.
      // This tells it to parse the query portion of the URL.
      const parsedUrl = parse(req.url, true)
      const { pathname, query } = parsedUrl
 
      if (pathname.startsWith('/backend/')) {
        const newPath = pathname.replace('/backend','')
        const proxyOpts = {
          host: managementServerHost,
          port: managementServerPort,
          path: newPath,
          method: req.method,
          headers: req.headers,
          query: query
        }
        const proxyReq = http.request(proxyOpts, (proxyRes) => {
          // passthrough status code and headers
          res.writeHead(proxyRes.statusCode, proxyRes.headers);
          proxyRes.pipe(res);
        });
        req.pipe(proxyReq)

      } else {
        await handle(req, res, parsedUrl)
      }
    } catch (err) {
      console.error('Error occurred handling', req.url, err)
      res.statusCode = 500
      res.end('internal server error')
    }
  })
    .once('error', (err) => {
      console.error(err)
      process.exit(1)
    })
    .listen(port, () => {
      console.log(`> Ready on http://${hostname}:${port}`)
    })
})