ARG ELK_VERSION

# https://github.com/elastic/elasticsearch-docker
FROM docker.elastic.co/elasticsearch/elasticsearch:${ELK_VERSION}
RUN rm -rf /etc/localtime
RUN ln -s /usr/share/zoneinfo/Africa/Kigali /etc/localtime
RUN bin/elasticsearch-keystore create
