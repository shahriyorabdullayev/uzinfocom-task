Uzinfocom test topshirig'i

Ilovada kotlin dasturlash tilidan foydalanildi.
Ui Jetpack composeda chizildi.
Local db uchun Room'dan foydalandim.
DI uchun Koin kutubxonasi ishlatildi.
Map Sdk uchun YandexMapKit SDK tanladim.

Berilgan pois.json va trucks.json filelarni, birinchi marta ilova ochilganda local db'ga kiritib oldim ishlash qulay bo'lishi uchun va barcha 
foydalanilgan joylarda shu bazadan olib foydalandim.

Ilova 2 qismdan iborat va bu uchun BottomNavigation orqali tablarga ajratilgan. Birinchi oynada Poi'lar ko'rsatiladi, Poilarni kategoriya
bo'yicha filtrlash imkoniyati mavjud va Poilar miqdori juda ko'pligi sabab clustering funksiyasidan foydalanib clusterlandi. har bir group ustiga
bosilganda zoomin bo'ladi va Poilar ko'rinadi, Poi ustiga bosilganda ModalBottomsheetda Poi infosi ko'rinadi. Keyingi tab Order deb nomladim, bu oynda
trucks.json fileda berilgan ma'lumotlar bo'yicha car iconni interpolate animation qildim va bearing ham hisoblandi, car animation pause qilish imkoniyati ham mavjud.
