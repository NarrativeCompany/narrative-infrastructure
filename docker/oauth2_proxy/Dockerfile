FROM golang

RUN go get -d -v github.com/bitly/oauth2_proxy
RUN go install -v github.com/bitly/oauth2_proxy

COPY docker-entrypoint.sh /

RUN mkdir /conf

RUN chmod a+x /docker-entrypoint.sh

EXPOSE 4180

ENTRYPOINT ["/docker-entrypoint.sh"]

CMD ["oauth2_proxy", "-config", "/conf/oauth2_proxy.conf"]

