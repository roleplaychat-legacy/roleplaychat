# RoleplayChat
Chat created for RP means. It is diesign to be highly abstracted from any specific game to ensure simple implementation for different Minecraft versions or even different games.

| Example input                      | Default radius | Output                                         |
|------------------------------------|----------------|------------------------------------------------|
| Привет.                            | 18             | None: Привет.                                  |
| !Какие люди!                       | 36             | None (восклицает): Какие люди!                 |
| !!Помогите, кто-нибудь!            | 60             | None (кричит): Помогите, кто-нибудь!           |
| !!!Моя нога!!!                     | 80             | None (орёт): Моя нога!!!                       |
| =Тише, господа.                    | 9              | None (вполголоса): Тише, господа.              |
| ==Только никому не говори.         | 3              | None (шепчет): Только никому не говори.        |
| ===И как я в это вляпался…         | 1              | None (едва слышно): И как я в это вляпался…    |
| (( Отойду на 2 минуты, извините )) | 18             | None (OOC): (( Отойду на 2 минуты, извините )) |
| _Отойду на 2 минуты, извините      | 18             | None (OOC): (( Отойду на 2 минуты, извините )) |
| -ГМ, можешь подлететь?             | ∞              | None (to GM): (( ГМ, можешь подлететь? ))      |
| *улыбается как дурак               | 18             | * None улыбается как дурак                     |

[Downloads](https://github.com/roleplaychat/RoleplayChat/releases)

# Extensions
[RoleplayChatDices](https://github.com/roleplaychat/RoleplayChatDices) - extension that adds d* dices to the game, result message distance is controlled by RoleplayChatCore.
