FROM node:20-alpine AS builder
WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npm run build

FROM nginx:alpine
WORKDIR /usr/share/nginx/html

EXPOSE 80

COPY --from=builder /app/build .

CMD ["nginx", "-g", "daemon off;"]