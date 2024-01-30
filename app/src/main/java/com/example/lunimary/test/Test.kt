package com.example.lunimary.test

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunimary.design.LightAndDarkPreview
import dev.jeziellago.compose.markdowntext.MarkdownText

val markdownContent = """  
	# Sample  
	* Markdown  
	* [Link](https://example.com)  
	![Image](https://p1.ssl.qhimgs1.com/sdr/400__/t0101cc32a15d91a3f7.jpg)  
	<a href="https://www.google.com/">Google</a>  
""".trimIndent()

//Minimal example
@Preview(showBackground = true)
@Composable
fun MinimalExampleContent() {
    MarkdownText(markdown = markdownContent)
}

//Complex example
@Preview(showBackground = true)
@Composable
fun ComplexExampleContent() {
    MarkdownText(
        modifier = Modifier.padding(8.dp),
        markdown = markdownContent,
        style = TextStyle(
            color = Color.Blue,
            fontSize = 12.sp,
            lineHeight = 10.sp,
            textAlign = TextAlign.Justify,
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun SampleMarkdown() {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        item {
            MarkdownText(
                markdown = """
                            ## Custom font

                            This text is using OpenSans Regular.
                            
                            ---
                        
                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                modifier = Modifier.clickable {
                    Toast
                        .makeText(context, "On text click", Toast.LENGTH_SHORT)
                        .show()
                },
                disableLinkMovementMethod = true,
                markdown = """
                            ## Clickable item

                            Should appear a single toast when clicking on this item!
                            
                            ---
                        
                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                markdown = """
                            ## Justify

                            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                        
                        """.trimIndent(),
                style = TextStyle(
                    textAlign = TextAlign.Justify,
                )
            )
        }
        item {
            MarkdownText(
                markdown = """
                            ## Color
                            
                            This text should appears in Blue color.
                            
                        """.trimIndent(),
                style = TextStyle(
                    color = Color.Blue
                )
            )
        }
        item {
            MarkdownText(
                markdown = """
                           ---
                            __Advertisement :)__
                            
                            - __[nodeca](https://nodeca.github.io/pica/demo/)__ - high quality and fast image
                              resize in browser.
                            - __[babelfish](https://github.com/nodeca/babelfish/)__ - developer friendly
                              i18n with plurals support and easy syntax.
                            
                            You will like those projects!
                            
                            ---
                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                markdown = """
                            # h1 Heading 8-)
                            ## h2 Heading
                            ### h3 Heading
                            #### h4 Heading
                            ##### h5 Heading
                            ###### h6 Heading
                            
                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                markdown = """
                            ## Horizontal Rules

                            ___
                            
                            ---
                            
                            ***
                            
                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                markdown = """
                            ## Emphasis

                            **This is bold text**
                            
                            __This is bold text__
                            
                            *This is italic text*
                            
                            _This is italic text_
                            
                            ~~Strikethrough~~
                            
                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                markdown = """
                            ## Links

                            [link text](http://dev.nodeca.com)
                            
                            [link with title](http://nodeca.github.io/pica/demo/ "title text!")
                            
                            Autoconverted link https://github.com/nodeca/pica (enable linkify to see)
                            
                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                modifier = Modifier.fillMaxWidth(),
                markdown = """
                            ## Images

                            ![Minion](https://octodex.github.com/images/minion.png)
                            ![Stormtroopocat](https://octodex.github.com/images/stormtroopocat.jpg "The Stormtroopocat")

                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                modifier = Modifier.fillMaxWidth(),
                markdown = """
                            Content
                            with
                            line
                            break

                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                modifier = Modifier.fillMaxWidth(),
                markdown = """
                            ## Tables

                            | Option | Description |
                            | ------ | ----------- |
                            | data   | path to data files to supply the data that will be passed into templates. |
                            | engine | engine to be used for processing templates. Handlebars is the default. |
                            | ext    | extension to be used for dest files. |
                            
                            Right aligned columns
                            
                            | Option | Description |
                            | ------:| -----------:|
                            | data   | path to data files to supply the data that will be passed into templates. |
                            | engine | engine to be used for processing templates. Handlebars is the default. |
                            | ext    | extension to be used for dest files. |
                            
                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                markdown = """
                            ## Lists

                            Unordered
                            
                            + Create a list by starting a line with `+`, `-`, or `*`
                            + Sub-lists are made by indenting 2 spaces:
                                - Marker character change forces new list start:
                                    * Ac tristique libero volutpat at
                                    + Facilisis in pretium nisl aliquet
                                    - Nulla volutpat aliquam velit
                            + Very easy!
                            
                            Ordered
                            
                            1. Lorem ipsum dolor sit amet
                            2. Consectetur adipiscing elit
                            3. Integer molestie lorem at massa
                            
                            
                            1. You can use sequential numbers...
                            1. ...or keep all the numbers as `1.`
                            
                            Start numbering with offset:
                            
                            57. foo
                            1. bar

                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                markdown = """
                            ## Blockquotes

                            > Blockquotes can also be nested...
                            >> ...by using additional greater-than signs right next to each other...
                            > > > ...or with spaces between arrows.

                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                markdown = """
                            ## Code

                            Inline `code`
                            
                            Indented code
                            
                                // Some comments
                                line 1 of code
                                line 2 of code
                                line 3 of code
                            
                            
                            Block code "fences"
                            
                            ```
                            Sample text here...
                            ```
                            
                            more code here..
                            
                            ``` js
                            var foo = function (bar) {
                              return bar++;
                            };
                            
                            console.log(foo(5));
                            ```
                        """.trimIndent(),
            )
        }
        item {
            MarkdownText(
                markdown = "\n# HTML SECTION ${
                    String(
//                        resources.openRawResource(R.raw.html).readBytes()
                    )
                }",
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun SnackbarPreview() {
    Snackbar(
        modifier = Modifier,
        action = {
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = "Ok"
            )
        },
        dismissAction = {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "Cancel"
            )
        },
        content = {
            Text(text = "支持Markdown语法，前往学习Markdown?")
        }
    )
}