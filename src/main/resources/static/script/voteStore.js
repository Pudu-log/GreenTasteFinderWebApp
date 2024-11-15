import {func} from "/static/script/voteStoreFunc.js";

$(function () {

    let param = {
        date: document.querySelector("input[name=title-date]").value
    };
    let url = "/storeInfo/" + param.date;

    func.get.storeInfo(url, param)
        .then(function (res) {

            func.set.storeInfo(res.data);

        }).catch(function (error) {

            console.log(error);

        });

})