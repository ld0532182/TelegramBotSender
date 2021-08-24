# TelegramBotSender

Это бот для Telegram, который умеет вытаскивать посты из группы в ВК (на данный момент пост должен состоять из связки фото + текст) и пересылать их в Telegram.

Для его работы понадобится создать приложение в ВК, получив от него токен, а также бот в Telegram и токен к нему.

У бота две основные команды: subscribe и unsubscribe, которые вносят или удаляют пользователя из списка подписавшихся.

Раз в 25 секунд (по умолчанию) бот проверяет новые посты в конкретной группе ВК и, если они есть, рассылает их подписанным пользователям.

Бот создавался с целью практики, поэтому подразумевается, что будет использоваться только Long polling и 
пользователи группы в ВК будут отправлять посты только со связкой фото + текст.
