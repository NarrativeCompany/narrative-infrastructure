# NGINX testoath backend

Just a simple NGINX test deployment, meant to demo the oauth2_proxy functionality.

# Deployment

- Edit the files accordingly; you'll want to update the hostname/domain information accordingly. Replace all examples of `TESTOAUTH.EXAMPLE.COM` with `testoauth.yourdomainhere.org`. Example sed:

```
# this is for OSX FYI... Slightly different than Linux.
sed -i '' 's/TESTOAUTH.EXAMPLE.ORG/testoauth.yourdomainhere.org/' *
```

- Deploy:

```
./deploy.sh
```

# Uninstall

- To uninstall/remove:

```
./remove.sh
```
