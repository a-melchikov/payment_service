FROM node:18-alpine

WORKDIR /app

COPY frontend/package.json .
COPY frontend/yarn.lock .

RUN yarn install

COPY frontend /app/

EXPOSE 5173

CMD ["yarn", "dev"]