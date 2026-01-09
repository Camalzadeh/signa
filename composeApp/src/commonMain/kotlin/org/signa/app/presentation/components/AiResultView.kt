import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.mikepenz.markdown.m3.Markdown

@Composable
fun AiResultView(content: String) {
    Markdown(
        content = content,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    )
}