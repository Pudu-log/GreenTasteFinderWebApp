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

        storeInfo: (data) => {
            let html = ``;

            console.log(data);

            let stores = data.storeList;
            let votes = data.voteList;
            let myPlaceId = data.myPlace;

            for (let store of stores) {
                let img = "";
                if (store?.photos?.length > 0){
                    img = `https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${store?.photos[0]?.photo_reference}&key=AIzaSyCd2XzSrgjduTBn27Faiz_H5pM8Xm81GoY`;
                }else{
                    img = `static/images/noImg.png`;
                }
                let lat1 = store.geometry.location.lat;
                let lng1 = store.geometry.location.lng;
                let lat2 = 35.1596124;
                let lng2 = 129.0601826;

                let myPlace = myPlaceId === store.place_id ? "on" : "";

                let distance = func.get.distance(lat1, lng1, lat2, lng2)
                html +=
                    `<div class="store-box ${myPlace}" data-place_id="${store.place_id}">
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
                                <span>${distance}m</span>
                            </div>
                        </div>
                        <div class="store-2wrap">
                            <div class="store-addr">
                                <span>${store.formatted_address}</span>
                            </div>
                            <div class="store-vote-wrap">
                        `;
                let cnt = 0;

                for (let vote of votes) {
                    if (vote.store_id === store.place_id) {
                        cnt++;
                    }
                }

                html +=
                    `<span>${cnt}명</span>
                    </div>
                    <div class="store-star">
                        <span>★ ${store?.rating ?? 0}</span>
                    </div>`;

                if (store?.current_opening_hours?.open_now) {
                    html +=
                        `<div class="store-status border-bl-10 border-br-10 on">
                            <span>운영중</span>
                        </div>`;
                } else {
                    html +=
                        `<div class="store-status border-bl-10 border-br-10">
                            <span>종료</span>
                        </div>`;
                }
                html +=
                    `<div class="store-vote-list">
                            <ul>`;

                cnt = 0;
                for (let vote of votes) {
                    if (vote.store_id === store.place_id) {
                        cnt ++;
                        html += `<li>${cnt}. ${vote.name}</li>`;
                    }
                }
                html += `
                            </ul>
                        </div>
                    </div>
                </div>`;
            }

            document.querySelector(".store-content").innerHTML = html;
        },
        vote: (date, place_id, type) => {

            let param = {};
            let url = "/vote/" + date + "/" + place_id + "/" + type;

            axios.post(url, param, {
                "Content-Type": "application/x-www-form-urlencoded",
            }).then(function (res) {
                console.log(res);
                if (res.data > -1) {
                    func.get.storeInfo();
                    func.set.boxStatus(place_id);
                } else if (res.data === -1) {
                    alert("투표중 오류가 발생하였습니다");
                } else if (res.data === -2) {
                    alert("12:45분이 지나 투표 할 수 없습니다.");
                }
            }).catch(function (error) {
                console.log(error);
            });
        },

        boxStatus: (place_id) => {
            const storeBoxs = document.querySelectorAll(".store-box");

            for (let storeBox of storeBoxs) {
                if (storeBox.dataset.place_id === place_id) {
                    storeBox.classList.add("on");
                    storeBox.focus();
                } else {
                    storeBox.classList.remove("on");
                }
            }
        }
    }

}


