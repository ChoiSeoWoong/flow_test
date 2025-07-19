<script>
    $(document).ready(function () {
        let maxCount = 200;
        let currentCount = 3; // 초기 존재 개수
        let customExtensions = {}; // { extName: fileId }

        // 1. 고정 확장자 체크박스 처리
        $('.fixed-ext').on('change', function () {
            const ext = $(this).val();
            const isChecked = $(this).is(':checked');

            if (isChecked) {
                $.post("/api/file-extension/fix", { fileExtensionName: ext })
                    .fail(() => alert("고정 확장자 추가 실패"));
            } else {
                const fileId = $(this).data("fileid");
                $.ajax({
                    url: "/api/file-extension/fix/" + fileId,
                    type: "DELETE",
                    success: function () {},
                    error: function () {
                        alert("고정 확장자 삭제 실패");
                    }
                });
            }
        });

    // 새로고침 시 체크된 값 유지
    function restoreFixedExtensions(checkedExtensionsWithId) {
        checkedExtensionsWithId.forEach(function (item) {
            $(`.fixed-ext[value="${item.name}"]`).prop('checked', true).data("fileid", item.id);
        });
    }

    // 2. 커스텀 확장자 추가
    $('#add-custom-ext').on('click', function () {
        const inputVal = $('#custom-ext-input').val().trim();

        if (inputVal === "") {
            return;
        }

        if (inputVal.length > 20) {
            alert("확장자는 20자 이하로 입력해주세요.");
            return;
        }

        if (Object.keys(customExtensions).includes(inputVal)) {
            alert("이미 존재하는 확장자입니다.");
            return;
        }

        if (currentCount >= maxCount) {
            alert("확장자 개수가 200개를 초과하였습니다.");
            return;
        }

        $.ajax({
            url: "/api/file-extension/custom",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ extensionName: inputVal }),
            success: function () {
                const fileId = Date.now(); // 서버 응답 값으로 대체 필요
                customExtensions[inputVal] = fileId;
                const extItem = $(`
                    <span class="custom-extension" data-id="${fileId}">
                      ${inputVal} <button class="remove-ext">X</button>
                    </span>
                `);
                $('#custom-ext-list').append(extItem);
                    currentCount++;
                    updateCountLabel();
                    $('#custom-ext-input').val('');
                },
                error: function () {
                    alert("커스텀 확장자 추가 실패");
                }
        });
    });

    // 3. 커스텀 확장자 삭제
    $('#custom-ext-list').on('click', '.remove-ext', function () {
        const extItem = $(this).closest('.custom-extension');
        const fileId = extItem.data('id');
        const extName = extItem.text().replace('X', '').trim();

        $.ajax({
            url: "/api/file-extension/custom/" + fileId,
            type: "DELETE",
            success: function () {
                delete customExtensions[extName];
                extItem.remove();
                currentCount--;
                updateCountLabel();
            },
            error: function () {
                alert("커스텀 확장자 삭제 실패");
            }
        });
    });

    function updateCountLabel() {
        $('#ext-count').text(`현재 존재하는 확장자 개수: ${currentCount} / 총 200`);
    }

    // 초기 데이터 복원 예시
    // restoreFixedExtensions([{ name: "exe", id: 101 }, { name: "js", id: 102 }]);
});
</script>