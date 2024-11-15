import {func} from "/static/script/voteStoreFunc.js";

$(function () {



    func.get.storeInfo();

    document.querySelector("input[name=title-date]").addEventListener("change", function () {
        func.get.storeInfo();
    });

    $(document).on("click", ".store-vote-wrap span", function (){
        this.parentNode.nextElementSibling.nextElementSibling.classList.toggle("on");
    });


})