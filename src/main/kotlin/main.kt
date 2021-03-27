package main

import com.google.gson.Gson
import com.jessecorbett.diskord.api.model.UserStatus
import com.jessecorbett.diskord.api.rest.EmbedAuthor
import com.jessecorbett.diskord.api.rest.EmbedImage
import com.jessecorbett.diskord.api.websocket.model.ActivityType
import com.jessecorbett.diskord.api.websocket.model.UserStatusActivity
import com.jessecorbett.diskord.dsl.*
import com.jessecorbett.diskord.util.authorId
import com.jessecorbett.diskord.util.words
import util.encrypting.Main
import json.*
import util.*
import util.mhApi.rest
import util.mhApi.socket

val gson = Gson()
val config = getConfig()

suspend fun main(){
    bot(config?.token.toString()){
        started {
            setStatus(UserStatus.ONLINE, activity = UserStatusActivity(".help || ${it.guilds.size} серверов", ActivityType.GAME))
            println("Ссылка для приглашения ${it.user.username}: https://discord.com/api/oauth2/authorize?client_id=${it.user.id}&permissions=8&scope=bot")
        }
        commands(config?.prefix.toString()){
            command("login"){// логин
                delete()
                val login = gson.fromJson(rest().checkLogin(this.words[1], this.words[2]), login::class.java)
                if(login.status == "ok"){
                    reply("Успешно залогинился")

                    reply("Добавляю в дб...")
                    val user = Main().base64Encrypt(this.words[1])
                    val password = Main().base64Encrypt(this.words[2])
                    val jsonObject = gson.toJson(userModel(user.toString(), password.toString()))

                    dbPut(jsonObject, authorId)
                    reply("Всё готово, можете узнать ваш баланс коммандой .wallet!")
                }
                else{
                    reply("Проверьте ваш пароль...")
                }
            }
            command("wallet"){// кошель
                    val dbObject = dbGet(authorId)
                    val jsonObject = gson.fromJson(dbObject, userModel::class.java)
                    val user = Main().base64Decrypt(jsonObject.username).toString()
                    val password = Main().base64Decrypt(jsonObject.password).toString()

                    val login = gson.fromJson(rest().checkLogin(user, password), login::class.java)
                    if(login.status == "ok"){
                        reply("Успешно залогинился")
                        val info = gson.fromJson(rest().checkInfo(login.token), info::class.java)
                        val jsonPrice = currentPrice()
                        val priceObject = gson.fromJson(jsonPrice, currentPrice::class.java)

                        val ducoUsdPrice = priceObject.bpi.USD.rate_float * 0.00000001
                        val balanceusd = info.user.balance.toDouble() * ducoUsdPrice

                        val authorr = author
                        val avatar = "https://cdn.discordapp.com/avatars/${authorr.id}/${authorr.avatarHash}.png"
                        if(info.status == "ok"){
                            reply{
                                color = 0x808080
                                author = EmbedAuthor("Информация о кошелке ${authorr.username}")
                                field("Ник", info.user.username, true)
                                field("Баланс", "%.8f Պ".format(info.user.balance.toDouble()), true)
                                field("Баланс в USD", "%.4f $".format(balanceusd), true)
                                field("Почта", info.user.email, true)
                                thumbnail = EmbedImage(avatar)
                            }
                        }
                        else{
                            reply("походу mh, упал")
                        }
                    }
                    else{
                        reply("Измените ваш пароль...")
                    }
            }
            command("network"){// инфа о сети
                val info = gson.fromJson(rest().checkServer(), mhServer::class.java)

                reply{
                    color = 0x808080
                    author = EmbedAuthor("Информация о сети", "https://mhc.lapki.dev")
                    field("Количество блоков", info.network.blocks.toString(), true)
                    field("Сложность", info.network.difficulty.toString(), true)
                    field("Всего монет", info.network.total_emission.toString(), true)
                    field("Количество зарегестрированных пользователей", info.network.users.toString(), true)
                    thumbnail = EmbedImage("https://mhcoin.s3.filebase.com/avatar.jpg")
                }
            }
            command("server"){// инфа о сервере
                val info = gson.fromJson(rest().checkServer(), mhServer::class.java)

                reply{
                    color = 0x808080
                    author = EmbedAuthor("Информация о сервере", "https://mhc.lapki.dev/")
                    field("Использование CPU", "${info.server.cpu}%", true)
                    field("Использование RAM", "${info.server.ram}%", true)
                    field("Версия сервера", info.server.version.toString(), true)
                    thumbnail = EmbedImage("https://mhcoin.s3.filebase.com/avatar.jpg")
                }
            }
            command("reg"){// регистрация
                delete()
                val result = socket().regApi(this.words)
                if(result.size == 1)
                {
                    reply("зарегал")

                    reply("Добавляю в дб...")
                    val user = Main().base64Encrypt(this.words[1])
                    val password = Main().base64Encrypt(this.words[2])
                    val jsonObject = gson.toJson(userModel(user.toString(), password.toString()))

                    dbPut(jsonObject, authorId)
                    reply("Всё готово, можете узнать ваш баланс коммандой .wallet!")
                }
                else{
                    reply("ошиб очка: ${result[1]}")
                }
            }
            command("delete"){// удалиться из бд
                if(dbDelete(authorId) == "null"){
                    reply("успешно удалил вас из бд")
                }
                else{
                    reply("какая-то ошиб очка")
                }
            }
            command("help"){// это хелп
                val avatar = "https://mhcoin.s3.filebase.com/avatar.jpg"
                if(this.words.size == 1){
                    reply("напиши .help 1 или .help 2")
                }
                if(this.words[1] == "1"){
                    reply{
                        color = 0x808080
                        thumbnail = EmbedImage(avatar)
                        footer("<> - не  требуется || 1 страница из 2")
                        field("USER INFO", ".login <ник> <пароль> - войти в аккаунт\n\n" +
                                ".reg <ник> <пароль> <почта> - зарегестрироваться\n\n" +
                                ".delete - удалить ваш аккаунт из нашей дб\n\n" +
                                ".wallet - информация о кошелке\n\n" +
                                ".transfer <получатель> <сумма> - перевод денег другому пользователю", false)
                    }
                }
                else{
                    if(this.words[1] == "2"){
                        reply{
                            color = 0x808080
                            thumbnail = EmbedImage(avatar)
                            footer("2 страница из 2")
                            field("INFORMATION", ".top - список 10 самых богатых пользователей\n\n" +
                                    ".network - информация о сети\n\n" +
                                    ".server - информация о сервере", false)
                        }
                    }
                }
            }
            command("top"){// топ 10 богачей
                val jsonRich = rest().checkRich()
                val jsonObject = gson.fromJson(jsonRich, rich::class.java)

                val avatar = "https://mhcoin.s3.filebase.com/avatar.jpg"
                reply{
                    color = 0x808080
                    thumbnail = EmbedImage(avatar)
                    for(i in 0..9){
                        field("${i + 1} место", "Ник: ${jsonObject.users[i].username}, баланс ${jsonObject.users[i].balance}", false)
                    }
                }
            }
            command("transfer"){// переводы
                val dbObject = dbGet(authorId)
                val jsonObject = gson.fromJson(dbObject, userModel::class.java)
                val user = Main().base64Decrypt(jsonObject.username).toString()
                val password = Main().base64Decrypt(jsonObject.password).toString()

                val login = gson.fromJson(rest().checkLogin(user, password), login::class.java)
                val avatar = "https://mhcoin.s3.filebase.com/avatar.jpg"
                if(login.status == "ok"){
                    reply("Залогинился")
                    val transferJson = rest().transfer(login.token, this.words[1], this.words[2])
                    val transfer = gson.fromJson(transferJson, transferModel::class.java)
                    if(transfer.status == "error"){
                        reply {
                            thumbnail = EmbedImage(avatar)
                            color = 0x808080
                            author = EmbedAuthor("Информация о транзакции")
                            field("Статус", transfer.status, false)
                            field("Ошибка", transfer.error, false)
                        }
                    }
                    else{
                        reply {
                            thumbnail = EmbedImage(avatar)
                            color = 0x808080
                            author = EmbedAuthor("Информация о транзакции")
                            field("Статус", transfer.status, false)
                            field("Ошибка", "нету", false)
                        }
                    }
                }
                else{
                    reply("Измените ваш пароль...")
                }
            }
        }
    }
}
