#!/bin/sh

echo "Starting entrypoint script..."

echo "Renewing"
certbot renew --nginx || true


echo "Assigning and Deploying"

certbot --nginx --email EckCiting@outlook.com --agree-tos --no-eff-email -d tdes.chnnhc.com --redirect --non-interactive || true

echo "Starting nginx in entrypoint"
nginx -s quit

sleep 2

exec nginx -g "daemon off;"


