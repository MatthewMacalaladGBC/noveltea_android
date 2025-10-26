package ca.gbc.comp3074.noveltea_app.data

import ca.gbc.comp3074.noveltea_app.model.Book

// Mock Database object to hold dummy book information
// For actual app, book data will be pulled from an API (i.e. Open Library API)
object MockDataset {
    fun getBooks(): List<Book> = listOf(
        Book(
            1, "The Hate U Give", "Angie Thomas", 4.3,
            """
                Sixteen-year-old Starr Carter moves between two worlds: the poor neighborhood where she lives and the fancy suburban prep school she attends. The uneasy balance between these worlds is shattered when Starr witnesses the fatal shooting of her childhood best friend Khalil at the hands of a police officer. Khalil was unarmed.

                Soon afterward, his death is a national headline. Some are calling him a thug, maybe even a drug dealer and a gangbanger. Protesters are taking to the streets in Khalil’s name. Some cops and the local drug lord try to intimidate Starr and her family. What everyone wants to know is: what really went down that night? And the only person alive who can answer that is Starr.

                But what Starr does—or does not—say could upend her community. It could also endanger her life.
            """.trimIndent(),
            "https://ia801705.us.archive.org/view_archive.php?archive=/29/items/l_covers_0008/l_covers_0008_04.zip&file=0008047722-L.jpg"
        ),
        Book(
            2, "The Two Towers", "J.R.R. Tolkien", 4.4,
            """
                Frodo and the Companions of the Ring have been beset by danger during their quest to prevent the Ruling Ring from falling into the hands of the Dark Lord by destroying it in the Cracks of Doom. They have lost the wizard, Gandalf, in the battle with an evil spirit in the Mines of Moria; and at the Falls of Rauros, Boromir, seduced by the power of the Ring, tried to seize it by force. While Frodo and Sam made their escape the rest of the company were attacked by Orcs. Now they continue their journey alone down the great River Anduin – alone, that is, save for the mysterious creeping figure that follows wherever they go.
            """.trimIndent(),
            "https://covers.openlibrary.org/b/id/14627082-L.jpg"
        ),
        Book(
            3, "The Alchemist", "Paulo Coelho", 3.8,
            """
                The Alchemist details the journey of a young Andalusian shepherd boy named Santiago. Santiago, believing a recurring dream to be prophetic, decides to travel to the pyramids of Egypt to find treasure. On the way, he encounters love, danger, opportunity and disaster. One of the significant characters that he meets is an old king named Melchizedek who tells him that "When you want something, all the universe conspires in helping you to achieve it." This is the core philosophy and motif of the book. 
            """.trimIndent(),
            "https://covers.openlibrary.org/b/id/15121528-L.jpg"
        ),
        Book(
            4, "The Great Gatsby", "F. Scott Fitzgerald", 4.0,
            """
                Here is a novel, glamorous, ironical, compassionate – a marvelous fusion into unity of the curious incongruities of the life of the period – which reveals a hero like no other – one who could live at no other time and in no other place. But he will live as a character, we surmise, as long as the memory of any reader lasts.

                "There was something gorgeous about him, some heightened sensitivity to the promises of life.... It was an extraordinary gift for hope, a romantic readiness such as I have never found in any other person and which it is not likely I shall ever find again."

                It is the story of this Jay Gatsby who came so mysteriously to West Egg, of his sumptuous entertainments, and of his love for Daisy Buchanan – a story that ranges from pure lyrical beauty to sheer brutal realism, and is infused with a sense of the strangeness of human circumstance in a heedless universe.

                It is a magical, living book, blended of irony, romance, and mysticism. 
            """.trimIndent(),
            "https://ia600100.us.archive.org/view_archive.php?archive=/5/items/l_covers_0012/l_covers_0012_36.zip&file=0012364437-L.jpg"
        )
    )
}