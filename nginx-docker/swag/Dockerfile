FROM nginx:alpine
RUN apk update && apk add bash
RUN rm -rf /etc/localtime
RUN ln -s /usr/share/zoneinfo/Africa/Kigali /etc/localtime
