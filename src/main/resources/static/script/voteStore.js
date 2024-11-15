import {func} from "/static/script/voteStoreFunc.js";

$(function () {



    func.get.storeInfo();

    document.querySelector("input[name=title-date]").addEventListener("change", function () {
        let param = {
            date: document.querySelector("input[name=title-date]").value
        };
        let url = "/storeInfo/" + param.date;

        func.get.storeInfo(url, param);
    });


})