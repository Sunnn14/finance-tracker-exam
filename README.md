# Personal Finance Tracker — Явцын шалгалтын мини төсөл

Kotlin + Jetpack Compose / Material 3 / Navigation Compose / Room / Repository + ViewModel + StateFlow / Preferences DataStore / WorkManager.

## Android Studio-д нээх
1. ZIP-ийг Extract All хийнэ.
2. Android Studio → Open → энэ хавтсыг сонгоно.
3. Gradle Sync хийх (интернет хэрэгтэй байж болно). JDK 17 ашиглана.
4. Android emulator (API 26+) сонгоод Run дарна.

> Төсөлд Gradle wrapper-ийн binary JAR ороогүй. Android Studio суулгасан Gradle ашиглах эсвэл wrapper-ийг үүсгэнэ. Энэ орчинд Android SDK ашиглан compile хийж туршаагүй.

## Бүтэц
- `data/local`: Room Entity, DAO, Database
- `data/settings`: DataStore preference
- `data/FinanceRepository.kt`: өгөгдлийн интерфейс
- `ui/FinanceViewModel.kt`: StateFlow, CRUD
- `ui/screens`: 3 Compose дэлгэц
- `navigation`: NavHost routes
- `worker`: сануулах мэдэгдэл болон төлөвлөлт

## 20 онооны шалгах тест
1. Шинээр нээхэд бүртгэл байхгүй: НЭМЭХ ажиллана, бусад бүртгэлтэй ажиллах товч идэвхгүй.
2. Орлого (Цалин 2000000), зарлага (Хоол 15000, Унаа 3000) нэмнэ.
3. Үндсэн дэлгэц нэг бүртгэл харуулна; мөнгөн дүн **** ₮; мөнгөн дүн дээр дарахад дэлгэнэ.
4. ӨМНӨХ, ДАРААХ товчийг шалгана.
5. ЗАСАХ болон карт дээр удаан дарах үйлдлийг шалгана.
6. УСТГАХ дарж ҮГҮЙ болон ТИЙМ сонголтыг тус тус шалгана.
7. Тохиргооноос Бүгд / Орлого / Зарлага сонгоод аппыг дахин нээж шалгана.
8. Утсыг хөндлөн эргүүлж хоёр баганатай харагдацыг шалгана.
9. Мэдэгдлийн зөвшөөрлийг өгнө. Апп арын төлөвт ороход 24 цагийн initial delay-тай periodic work төлөвлөгдөнө; мэдэгдэл дээр дарахад MainActivity нээгдэнэ.

**Анхаар:** WorkManager ажиллах цаг exact биш; батарей, Doze, системийн хуваарилалтаас хамаарч хойшилж болно. Android 13+ дээр мэдэгдлийн зөвшөөрөл шаардлагатай. APK, emulator дээр бодитоор тестлэх хэрэгтэй.

## Хүлээлгэн өгөх
Кодын ZIP/GitHub repo **ба** OBS ашиглан бичсэн аппын ажиллагааны дэлгэцийн видео — хоёуланг явуулна.
