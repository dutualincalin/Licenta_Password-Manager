# Build
FROM node:20.2.0-alpine AS build

# Install CLI
WORKDIR /app
RUN npm install -g @angular/cli@16.0.4

# Install dependencies
COPY package.json .
RUN npm install

# Build
COPY . .
CMD ng serve --host 0.0.0.0 --port 4200
