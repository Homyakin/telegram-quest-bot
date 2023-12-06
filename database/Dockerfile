FROM postgres:14.4-alpine

COPY init.sql /docker-entrypoint-initdb.d/

ENV POSTGRES_USER=dev
ENV POSTGRES_PASSWORD=dev
ENV POSTGRES_DB=quest

ENV TZ=Europe/Moscow

EXPOSE 5432
