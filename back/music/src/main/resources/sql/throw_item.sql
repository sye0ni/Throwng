INSERT throwng.throw_item(created_at, content, item_image, location_point, status, user_id, song_id, zipcode_id)
VALUES(NOW(), "출근 길에 듣기 좋아요~", "https://d2qe2q8v1imxmh.cloudfront.net/throwng-dev/%EB%82%98%EB%8A%94%20%EC%95%84%ED%94%88%20%EA%B1%B4%20%EB%94%B1%20%EC%A7%88%EC%83%89%EC%9D%B4%EB%8B%88%EA%B9%8C.jpg", ST_GeomFromText('POINT(35.20673614550716 126.87817654137028)', 4326), "VISIBLE", 1, 1, 1552);

INSERT throwng.throw_item(created_at, content, item_image, location_point, status, user_id, song_id, zipcode_id)
VALUES(NOW(), "쉽지 않았지~", "https://d2qe2q8v1imxmh.cloudfront.net/throwng-dev/EASY.jpg", ST_GeomFromText('POINT(35.20667402786965 126.87471730485265)', 4326), "VISIBLE", 1, 2, 1552);

INSERT throwng.throw_item(created_at, content, item_image, location_point, status, user_id, song_id, zipcode_id)
VALUES(NOW(), "너한테 슈퍼 이끌림", "https://throwng-dev.s3.ap-northeast-2.amazonaws.com/Magnetic.jpg", ST_GeomFromText('POINT(35.20674655565142 126.87512352772394)', 4326), "VISIBLE", 1, 3, 1552);