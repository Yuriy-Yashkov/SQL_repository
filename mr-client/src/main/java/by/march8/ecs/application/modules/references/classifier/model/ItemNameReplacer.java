package by.march8.ecs.application.modules.references.classifier.model;

/**
 * @author Andy 15.01.2019 - 7:14.
 */
public final class ItemNameReplacer {
    public static String transform(String string) {
        String out = string.trim().toLowerCase()
                .replace("  ", " ")
                .replace("a", "а")
                .replace("e", "е")
                .replace("o", "о")
                .replace("m", "м")
                .replace("c", "с")

                .replace("т. ж", "т ж")
                .replace("т.ж", "т ж")

                .replace("т. м", "т м")
                .replace("т.м", "т м")
                .replace("н.ж", "н ж")
                .replace("н. ж", "н ж")
                .replace("т. ", "т ")

                .replace(".2", " 2")
                .replace(".3", " 3")
                .replace(".", "")


                .replace("дев", "для девочки")
                .replace("мал", "для мальчика")
                .replace("комплект спорт", "комплект спортивный")
                .replace("юки спорт", "юки спортивные")
                .replace("ы спорт", "ы спортивные")
                .replace("юм спорт", "юм спортивный")
                .replace("ик спорт", "ик спортивный")
                .replace("ер спорт", "ер спортивный")
                .replace("ка спорт", "ка спортивная")

                .replace("юм купальн", "юм купальный")
                .replace("а ночн", "а ночная")
                .replace("и пиж", "и пижамные")
                .replace("т укороченные", "и пижамные")

                .replace("и дет", "и детские")
                .replace("и жен", "и женские")
                .replace("и муж", "и мужские")

                .replace("а жен", "а женская")
                .replace("а дет", "а детская")
                .replace("а муж", "а мужская")

                .replace("е жен", "е женское")
                .replace("е дет", "е детское")
                .replace("е муж", "е мужское")

                .replace("я жен", "я женская")
                .replace("я дет", "я детская")

                .replace("ая жен", "ая женская")
                .replace("ая дет", "ая детская")
                .replace("ие муж", "ие мужские")

                .replace("ые жен", "ые женские")
                .replace("ые муж", "ые мужские")
                .replace("ые дет", "ые детские")

                .replace("р дет", "р детский")
                .replace("р жен", "р женский")
                .replace("р муж", "р мужской")


                .replace("ый жен", "ый женский")
                .replace("ый муж", "ый мужской")
                .replace("ый дет", "ый детский")

                .replace("т дет", "т детский")
                .replace("т жен", "т женский")
                .replace("т муж", "т мужской")

                .replace("к дет", "к детский")
                .replace("к жен", "к женский")
                .replace("к муж", "к мужской")

                .replace("м дет", "м детский")
                .replace("м жен", "м женский")
                .replace("м муж", "м мужской")

                .replace("ы жен", "ы женские")
                .replace("ы муж", "ы мужские")
                .replace("ы дет", "ы детские")

                .replace("н муж", "н мужской")
                .replace("н дет", "н детский")
                .replace("н жен", "н женский")
                .replace("с жен", "с женский")
                .replace("п жен", "п женский")

                .replace("  ", " ")


                .replace("гимнастдля", "гимнастический для")
                .replace("купгимнд/", "Купальник гимнастический для")
                .replace("девочкиподрост", "девочки подростковое")
                .replace("ая укороч", "ая укороченная")
                .replace("ка подрост", "ка подростковый")
                .replace("ки-подр", "ки")
                .replace("ие спорт", "ие спортивные")
                //.replace("скиеские", "ские")
                .replace("скоеские", "ские")
                .replace("скиеское", "ские")

                .replace("скийский", "ский")
                .replace("скойской", "ской")
                .replace("скиеские", "ские")
                .replace("скаяская", "ская")
                .replace("ивныйивный", "ивный")
                .replace("ивныеивные", "ивные")
                .replace("ныйый", "ный")
                .replace("аяая", "ая");

        return out.substring(0, 1).toUpperCase() + out.substring(1).toLowerCase();
    }
}
