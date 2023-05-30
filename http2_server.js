const spdy = require('spdy');
const fs = require('fs');

const options = {
  key: fs.readFileSync('server.key'),
  cert: fs.readFileSync('server.crt')
};

spdy.createServer(options, (req, res) => {
  res.writeHead(200);
  res.end('Hello, world!');
}).listen(443);

