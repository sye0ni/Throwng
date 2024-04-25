INSERT throwng.throw_item(created_at, content, item_image, location_point, status, user_id, song_id, zipcode_id)
VALUES(NOW(), "출근 길에 듣기 좋아요~", "https://d2qe2q8v1imxmh.cloudfront.net/throwng-dev/%EB%82%98%EB%8A%94%20%EC%95%84%ED%94%88%20%EA%B1%B4%20%EB%94%B1%20%EC%A7%88%EC%83%89%EC%9D%B4%EB%8B%88%EA%B9%8C.jpg", ST_GeomFromText('POINT(37.123456 127.123456)', 4326), "VISIBLE", 1, 1, 1);

INSERT throwng.throw_item(created_at, content, item_image, location_point, status, user_id, song_id, zipcode_id)
VALUES(NOW(), "쉽지 않았지~", "https://d2qe2q8v1imxmh.cloudfront.net/throwng-dev/EASY.jpg", ST_GeomFromText('POINT(37.126455 127.129455)', 4326), "VISIBLE", 1, 2, 1);