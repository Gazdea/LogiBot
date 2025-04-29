window.addEventListener("DOMContentLoaded", () => {
    // Подождём чуть-чуть, чтобы Telegram успел внедрить WebApp API
    setTimeout(() => {
        if (!window.Telegram || !window.Telegram.WebApp) {
            alert("Открой страницу через Telegram Web App");
            return;
        }

        const tg = window.Telegram.WebApp;
        tg.ready();

        const user = tg.initDataUnsafe?.user;
        const userInfoDiv = document.getElementById("user-info");

        if (user) {
            userInfoDiv.innerHTML = `
                <h2>Информация о пользователе</h2>
                <ul>
                  <li><strong>ID:</strong> ${user.id}</li>
                  <li><strong>Имя:</strong> ${user.first_name}</li>
                  <li><strong>Фамилия:</strong> ${user.last_name || '—'}</li>
                  <li><strong>Юзернейм:</strong> @${user.username || '—'}</li>
                  <li><strong>Язык:</strong> ${user.language_code || '—'}</li>
                </ul>
            `;
        } else {
            userInfoDiv.innerHTML = `<p>Пользователь не определён. Убедитесь, что вы открыли Web App из Telegram.</p>`;
        }

        // Отправка запроса на сервер
        fetch(`/api/user-info/${user.id}`, {
            method: 'GET', // Используем GET, так как передаем параметр через URL
        })
            .then(res => res.json())
            .then(data => {
                // Отображаем результат запроса на странице
                const resultDiv = document.getElementById("result");
                resultDiv.innerHTML = `<p>Запрос успешно выполнен! Ответ от сервера: ${JSON.stringify(data)}</p>`;
            })
            .catch(err => {
                // Отображаем ошибку на странице
                const resultDiv = document.getElementById("result");
                resultDiv.innerHTML = `<p>Произошла ошибка при выполнении запроса: ${err}</p>`;
            });

    }, 200); // 200 мс достаточно
});
