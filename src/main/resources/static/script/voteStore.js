import {func} from "/static/script/voteStoreFunc.js";

$(function () {


    func.get.storeInfo();

    document.querySelector("input[name=title-date]").addEventListener("change", function () {
        let date = document.querySelector("input[name=title-date]").value;

        let now = new Date();
        let inputDate = new Date(date);

        if (inputDate > now){
            document.querySelector("input[name=title-date]").value = func.set.dateFormat(now);
            return;
        }

        func.get.storeInfo();
    });

    $(document).on("click", ".store-vote-wrap span", function (){
        this.parentNode.nextElementSibling.nextElementSibling.nextElementSibling.classList.toggle("on");
    });

    $(document).on("dblclick", ".store-box", function (){
        let date = document.querySelector("input[name=title-date]").value;

        let place_id = this.dataset.place_id;
        let type = this.classList.contains("on") ? "D" : "E";

        func.set.vote(date, place_id, type);
    });

    $(document).on("click", ".store-img", function (){
        //TODO: 기환씨가만든 상세페이지로
    });

})