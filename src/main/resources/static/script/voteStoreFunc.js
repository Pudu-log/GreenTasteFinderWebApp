export let func = {

    get: {

        storeInfo: () => {

            let param = {
                date: document.querySelector("input[name=title-date]").value
            };
            let url = "/storeInfo/" + param.date;

            axios.post(url, param, {
                "Content-Type": "application/x-www-form-urlencoded",
            }).then(function (res) {
                func.set.storeInfo(res.data);
            }).catch(function (error) {
                console.log(error);
            });
        },
        distance: (lat1, lon1, lat2, lon2) => {
            const EARTH_RADIUS_M = 6371000; // 지구 반지름 (미터 단위)

            // 위도와 경도를 라디안으로 변환하는 함수
            const toRadians = (degrees) => degrees * (Math.PI / 180);

            // 입력값 검증
            if (
                typeof lat1 !== "number" || isNaN(lat1) ||
                typeof lon1 !== "number" || isNaN(lon1) ||
                typeof lat2 !== "number" || isNaN(lat2) ||
                typeof lon2 !== "number" || isNaN(lon2)
            ) {
                throw new Error("위도와 경도 값은 숫자여야 합니다.");
            }

            // 위도와 경도를 라디안으로 변환
            const lat1Rad = toRadians(lat1);
            const lat2Rad = toRadians(lat2);
            const deltaLat = toRadians(lat2 - lat1);
            const deltaLon = toRadians(lon2 - lon1);

            // 하버사인 공식 적용
            const a =
                Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

            const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            // 거리 계산 (미터 단위로 반환) + 소수점 제거
            return Math.floor(EARTH_RADIUS_M * c);
        }

    },

    set: {

        storeInfo: (stores) => {
            let html = ``;
            console.log(stores);
            for (let store of stores) {
                let img = `https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${store?.photos[0]?.photo_reference}&key=AIzaSyCd2XzSrgjduTBn27Faiz_H5pM8Xm81GoY`;
                let lat1 = store.geometry.location.lat;
                let lng1 = store.geometry.location.lng;
                let lat2 = 35.1596124;
                let lng2 = 129.0601826;

                console.log(lat1, lng1, lat2, lng2);
                let distance = func.get.distance(lat1, lng1, lat2, lng2)
                html +=
                    `<div class="store-box">
                    <div class="store-img border-tl-10">
                        <img class="border-tl-10" src="${img}">
                    </div>
                    <div class="store-wrap">
                        <div class="store-title border-tr-10">
                            <span class="border-tr-10">${store.name}</span>
                        </div>
                        <div class="store-gbn">
                            <span>${store.types[0]}</span>
                        </div>
                        <div class="store-distance">
                            <span>${distance}M</span>
                        </div>
                    </div>
                    <div class="store-2wrap">
                        <div class="store-addr">
                            <span>${store.formatted_address}</span>
                        </div>
                        <div class="store-vote-wrap">
                            <span>홍길동 외 1명</span>
                        </div>
                `;
                    if (store?.current_opening_hours?.open_now){
                        html += `
                        <div class="store-status on">
                            <span>운영중</span>
                        </div>
                        `
                    }else{
                        html += `
                        <div class="store-status">
                            <span>마감</span>
                        </div>
                        `
                    }
                    html += `
                        <div class="store-vote-list">
                            <ul>
                                <li>홍길동</li>
                                <li>홍길순</li>
                                <li>홍길자</li>
                                <li>홍길자</li>
                                <li>홍길자</li>
                                <li>홍길자</li>
                                <li>홍길자</li>
                                <li>홍길자</li>
                                <li>홍길자</li>
                                <li>홍길자</li>
                                <li>홍길민</li>
                                <li>홍홍홍</li>
                            </ul>
                        </div>
                    </div>
                </div>`;
            }

            document.querySelector(".store-content").innerHTML = html;
        }
    }

}


