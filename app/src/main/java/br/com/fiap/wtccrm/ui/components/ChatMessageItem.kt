package br.com.fiap.wtccrm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import br.com.fiap.wtccrm.model.Message
import br.com.fiap.wtccrm.network.RetrofitInstance

@Composable
fun ChatMessageItem(msg: Message) {

    val isMine =
        msg.senderId ==
                RetrofitInstance.currentUserEmail

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),

        horizontalArrangement =
            if (isMine)
                Arrangement.End
            else
                Arrangement.Start
    ) {

        Column(
            horizontalAlignment =
                if (isMine)
                    Alignment.End
                else
                    Alignment.Start
        ) {

            Text(
                text = msg.senderId,
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .background(
                        color =
                            if (isMine)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant,

                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(
                        horizontal = 14.dp,
                        vertical = 10.dp
                    )
            ) {

                Text(
                    text = msg.content,

                    color =
                        if (isMine)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}