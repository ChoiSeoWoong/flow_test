$(document).ready(function () {
    let maxCount = 200;
    let currentCount = 0;
    const customExtensions = {};

    $.get("http://localhost:8080/api/file-extension/fix/all", function (data) {
        const container = $('#fixed-ext-container');
        data.forEach(ext => {
            const checkbox = $(`
                <label>
                    <input type="checkbox" class="fixed-ext"
                        value="${ext.fileExtensionName}"
                        ${ext.checked ? 'checked' : ''}
                        data-fileid="${ext.fileId || ''}">
                    ${ext.fileExtensionName}
                </label>
            `);
            container.append(checkbox);
        });
    });

    $('#fixed-ext-container').on('change', '.fixed-ext', function () {
        const $checkbox = $(this);
        const fileId = $checkbox.data("fileid");
        const isChecked = $checkbox.is(':checked');

        if (isChecked) {
            if (!fileId) {
                alert("fileId가 없어 등록할 수 없습니다.");
                return;
            }

            $.post("http://localhost:8080/api/file-extension/fix", { fileId: fileId })
                .done(function () {
                    console.log("등록 완료: fileId = " + fileId);
                })
                .fail(() => alert("고정 확장자 등록 실패"));
        } else {

            if (!fileId) {
                alert("fileId가 없어 삭제할 수 없습니다.");
                return;
            }

            $.ajax({
                url: "http://localhost:8080/api/file-extension/fix/" + fileId,
                type: "DELETE",
                success: function () {
                },
                error: function () {
                    alert("고정 확장자 삭제 실패");
                }
            });
        }
    });

    $.get("http://localhost:8080/api/file-extension/custom/all", function (data) {
        data.forEach(ext => {
            renderCustomExtension(ext.fileExtensionName, ext.fileId);
        });
        currentCount = data.length;
        updateCountLabel();

        // 버튼도 같이 동적으로 생성
        const buttonBox = $('#dynamic-buttons').empty();
        data.forEach(ext => {
            buttonBox.append(`<button name="${ext.fileExtensionName}">${ext.fileExtensionName}</button>`);
        });
    });

    // 3. 커스텀 확장자 추가
    $('#add-custom-ext').on('click', function () {
        const inputVal = $('#custom-ext-input').val().trim();
        if (!inputVal) return;
        if (inputVal.length > 20) {
            alert("확장자는 20자 이하로 입력해주세요.");
            return;
        }
        if (customExtensions[inputVal]) {
            alert("이미 존재하는 확장자입니다.");
            return;
        }
        if (currentCount >= maxCount) {
            alert("확장자 개수가 200개를 초과하였습니다.");
            return;
        }

        $.ajax({
            url: "http://localhost:8080/api/file-extension/custom",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                fileExtensionName: inputVal,
                fileExtensionNameListCount: currentCount
            }),
            success: function (newFileId) {
                renderCustomExtension(inputVal, newFileId);
                currentCount++;
                updateCountLabel();
                $('#custom-ext-input').val('');
                $('#dynamic-buttons').append(`<button name="${inputVal}">${inputVal}</button>`);
            },
            error: function () {
                alert("커스텀 확장자 추가 실패");
            }
        });
    });

    $('#custom-ext-list').on('click', '.remove-ext', function () {
        const extItem = $(this).closest('.custom-extension');
        const extName = extItem.data('name');
        const fileId = extItem.data('id');

        $.ajax({
            url: "http://localhost:8080/api/file-extension/custom/" + fileId,
            type: "DELETE",
            success: function () {
                delete customExtensions[extName];
                extItem.remove();
                $(`#dynamic-buttons button[name="${extName}"]`).remove();
                currentCount--;
                updateCountLabel();
            },
            error: function () {
                alert("커스텀 확장자 삭제 실패");
            }
        });
    });

    function renderCustomExtension(name, id) {
        const extItem = $(`
            <span class="custom-extension" data-id="${id}" data-name="${name}">
                ${name} <button class="remove-ext">X</button>
            </span>
        `);
        $('#custom-ext-list').append(extItem);
        customExtensions[name] = id;
    }

    function updateCountLabel() {
        $('#ext-count').text(`현재 개수 : ${currentCount} / 총 개수 : ${maxCount}`);
    }
});