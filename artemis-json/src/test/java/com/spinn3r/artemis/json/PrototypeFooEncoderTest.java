package com.spinn3r.artemis.json;

import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;
import com.google.common.base.Charsets;
import com.google.common.base.Stopwatch;
import com.spinn3r.artemis.util.io.FastByteArrayOutputStream;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@Ignore
public class PrototypeFooEncoderTest {

    int max = 3_100_000;

    String text = "Le Journal d'un voyage de Londres à Lisbonne (The Journal of a Voyage to Lisbon) est le dernier ouvrage écrit par Henry Fielding (1707-1754) alors que, malade et à bout de forces, accompagné de sa seconde épouse Mary Daniel (Mary Fielding), d'une de ses filles Eleanor Harriot, de l'amie de cette dernière Margaret Collier et de deux serviteurs, la femme de chambre Isabella Ash et le valet de pied William, il était en route pendant l'été de 1754 pour Lisbonne à bord du Queen of Portugal. Soumis aux caprices du commandant et aux aléas du temps, le navire, longtemps privé de vent, a dérivé sur la Tamise, puis longé la côte sud, et c'est seulement dans les toutes dernières pages du livre que se gonflent les voiles et que le véritable voyage commence. Ainsi, à bien des égards, le Journal de Fielding concerne plus les rives et rivages anglais que la traversée du golfe de Biscaye et l'arrivée au Portugal.\n" +
                    "\n" +
                    "Ce court ensemble se présente sous la forme d'une chronique au jour le jour où se mêlent les anecdotes du quotidien et nombre de considérations autant politiques que morales sur la société et l'humanité en général. Le ton en est général humoristique, mais pointe dans le récit un discret stoïcisme devant les souffrances endurées. S'y trouvent également discutés de nombreux sujets concernant le droit maritime et surtout la dernière action menée par Fielding en tant que magistrat, profession que par la force des choses il vient de quitter. Quelques portraits croustillants de drôlerie et parfois non dénués de préjugés insulaires parsèment le récit, mais comme dans les romans, le pittoresque est absent des descriptions dont, à de rares exceptions près, le style reste calqué sur le langage poétique obligé de la fin du xviie et du début du xviiie siècle.\n" +
                    "\n" +
                    "L'ironie traverse l'ouvrage de page en page, dirigée contre certains personnages de rencontre, mais surtout contre le narrateur, plus parodique que franchement satirique, toujours comique. Elle s'appuie sur plusieurs récits de voyage anglais, mais essentiellement sur les épopées d'Homère et de Virgile dont les héros, à des degrés divers, représentent le passager souffrant balloté sur les flots en quête d'une nouvelle patrie.\n" +
                    "\n" +
                    "Le journal de cette traversée a été publié à titre posthume en janvier 1755, soit une année après la mort de l'auteur et ironiquement dix mois avant le tremblement de terre qui a incité Voltaire à se préoccuper de la divine Providence.";

//    String text = "World War II (WWII or WW2), also known as the Second World War, was a global war that lasted from 1939 to 1945, though related conflicts began earlier. It involved the vast majority of the world's nations—including all of the great powers—eventually forming two opposing military alliances: the Allies and the Axis. It was the most widespread war in history, and directly involved more than 100 million people from over 30 countries. In a state of \"total war\", the major participants threw their entire economic, industrial, and scientific capabilities behind the war effort, erasing the distinction between civilian and military resources. Marked by mass deaths of civilians, including the Holocaust (in which approximately 11 million people were killed)[1][2] and the strategic bombing of industrial and population centres (in which approximately one million were killed, and which included the atomic bombings of Hiroshima and Nagasaki),[3] it resulted in an estimated 50 million to 85 million fatalities. These made World War II the deadliest conflict in human history.[4]\n" +
//                    "The Empire of Japan aimed to dominate Asia and the Pacific and was already at war with the Republic of China in 1937,[5] but the world war is generally said to have begun on 1 September 1939[6] with the invasion of Poland by Germany and subsequent declarations of war on Germany by France and the United Kingdom. From late 1939 to early 1941, in a series of campaigns and treaties, Germany conquered or controlled much of continental Europe, and formed the Axis alliance with Italy and Japan. Based on the Molotov–Ribbentrop Pact of August 1939, Germany and the Soviet Union partitioned and annexed territories of their European neighbours, Poland, Finland, Romania and the Baltic states. For a year starting in late June 1940, the United Kingdom and the British Commonwealth were the only Allied forces continuing the fight against the European Axis powers, with campaigns in North Africa and the Horn of Africa, the aerial Battle of Britain and the Blitz bombing campaign, as well as the long-running Battle of the Atlantic. In June 1941, the European Axis powers launched an invasion of the Soviet Union, opening the largest land theatre of war in history, which trapped the major part of the Axis' military forces into a war of attrition. In December 1941, Japan attacked the United States and European territories in the Pacific Ocean, and quickly conquered much of the Western Pacific.\n" +
//                    "The Axis advance halted in 1942 when Japan lost the critical Battle of Midway, near Hawaii, and Germany was defeated in North Africa and then, decisively, at Stalingrad in the Soviet Union. In 1943, with a series of German defeats on the Eastern Front, the Allied invasion of Italy which brought about Italian surrender, and Allied victories in the Pacific, the Axis lost the initiative and undertook strategic retreat on all fronts. In 1944, the Western Allies invaded German-occupied France, while the Soviet Union regained all of its territorial losses and invaded Germany and its allies. During 1944 and 1945 the Japanese suffered major reverses in mainland Asia in South Central China and Burma, while the Allies crippled the Japanese Navy and captured key Western Pacific islands.\n" +
//                    "The war in Europe ended with an invasion of Germany by the Western Allies and the Soviet Union culminating in the capture of Berlin by Soviet and Polish troops and the subsequent German unconditional surrender on 8 May 1945. Following the Potsdam Declaration by the Allies on 26 July 1945 and the refusal of Japan to surrender under its terms, the United States dropped atomic bombs on the Japanese cities of Hiroshima and Nagasaki on 6 August and 9 August respectively. With an invasion of the Japanese archipelago imminent, the possibility of additional atomic bombings, and the Soviet Union's declaration of war on Japan and invasion of Manchuria, Japan surrendered on 15 August 1945. Thus ended the war in Asia, cementing the total victory of the Allies.";

    Foo foo = new Foo( "Kevin", "Burton", text );


    @Test
    public void testEncodeUsingPrototype() throws Exception {

        PrototypeFooEncoder prototypeFooEncoder = new PrototypeFooEncoder( 50000, 50000 );

        ByteBuffer byteBuffer = prototypeFooEncoder.encode( foo );

        System.out.printf( "%s\n", toString( byteBuffer ));

        // ****

        Stopwatch stopwatch = Stopwatch.createStarted();

        for (int i = 0; i < max; i++) {
            byteBuffer = prototypeFooEncoder.encode( foo );
            prototypeFooEncoder.reset();
        }

        System.out.printf( "testEncodeUsingPrototype took: %s\n", stopwatch.stop() );

        System.gc();

    }

    @Test
    public void testEncodingUsingJackson() throws Exception {

        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream( 50000 );

        JacksonFooEncoder jacksonFooEncoder = new JacksonFooEncoder(fastByteArrayOutputStream);

        ByteBuffer byteBuffer = jacksonFooEncoder.encode( foo );

        //System.out.printf( "%s\n", toString( byteBuffer ));

        // ****

        Stopwatch stopwatch = Stopwatch.createStarted();

        for (int i = 0; i < max; i++) {
            byteBuffer = jacksonFooEncoder.encode( foo );
            fastByteArrayOutputStream.reset();
        }

        System.out.printf( "testEncodingUsingJackson took: %s\n", stopwatch.stop() );

        System.gc();


    }

    @Test
    @Ignore
    public void testJacksonEscapes() throws Exception {

        Foo foo = new Foo( "\"\n\\", "", "" );

        System.out.printf( "before: %s\n", foo.toString() );

        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream( 50000 );

        JacksonFooEncoder jacksonFooEncoder = new JacksonFooEncoder(fastByteArrayOutputStream);

        ByteBuffer byteBuffer = jacksonFooEncoder.encode( foo );

        System.out.printf( "%s\n", toString( byteBuffer ) );

    }

    @Test
    @Ignore
    public void testPrototypeOutput() throws Exception {

        PrototypeFooEncoder prototypeFooEncoder = new PrototypeFooEncoder( 50000, 50000 );

        ByteBuffer byteBuffer = prototypeFooEncoder.encode( foo );

        System.out.printf( "%s\n", toString( byteBuffer ) );

    }

    @Test
    public void testIdenticalOutput() throws Exception {

        // *** prototype

        PrototypeFooEncoder prototypeFooEncoder = new PrototypeFooEncoder( 50000, 50000 );

        ByteBuffer byteBuffer0 = prototypeFooEncoder.encode( foo );

        // *** jackson

        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream( 50000 );

        JacksonFooEncoder jacksonFooEncoder = new JacksonFooEncoder(fastByteArrayOutputStream);

        ByteBuffer byteBuffer1 = jacksonFooEncoder.encode( foo );

        assertEquals( toString( byteBuffer0 ), toString( byteBuffer1 ) );

    }

    @Test
    @Ignore
    public void testUTF8() throws Exception {

        Foo foo = new Foo( "위키백과, 우리 모두의 백과사전위키백과, 우리 모두의 백과사전", "Burton", "100 Washington Street, San Francisco, CA 94107" );

        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream( 50000 );

        JacksonFooEncoder jacksonFooEncoder = new JacksonFooEncoder(fastByteArrayOutputStream);

        ByteBuffer byteBuffer = jacksonFooEncoder.encode( foo );

        String str = new String( byteBuffer.array(), 0, byteBuffer.limit(), Charsets.UTF_8 );

        assertEquals( "{\"first_name\":\"위키백과, 우리 모두의 백과사전위키백과, 우리 모두의 백과사전\",\"last_name\":\"Burton\",\"address\":\"100 Washington Street, San Francisco, CA 94107\"}", str );

    }

    @Test
    @Ignore
    public void test1() throws Exception {

        String text = "Le Journal d'un voyage de Londres à Lisbonne (The Journal of a Voyage to Lisbon) est le dernier ouvrage écrit par Henry Fielding (1707-1754) alors que, malade et à bout de forces, accompagné de sa seconde épouse Mary Daniel (Mary Fielding), d'une de ses filles Eleanor Harriot, de l'amie de cette dernière Margaret Collier et de deux serviteurs, la femme de chambre Isabella Ash et le valet de pied William, il était en route pendant l'été de 1754 pour Lisbonne à bord du Queen of Portugal. Soumis aux caprices du commandant et aux aléas du temps, le navire, longtemps privé de vent, a dérivé sur la Tamise, puis longé la côte sud, et c'est seulement dans les toutes dernières pages du livre que se gonflent les voiles et que le véritable voyage commence. Ainsi, à bien des égards, le Journal de Fielding concerne plus les rives et rivages anglais que la traversée du golfe de Biscaye et l'arrivée au Portugal.\n" +
                        "\n" +
                        "Ce court ensemble se présente sous la forme d'une chronique au jour le jour où se mêlent les anecdotes du quotidien et nombre de considérations autant politiques que morales sur la société et l'humanité en général. Le ton en est général humoristique, mais pointe dans le récit un discret stoïcisme devant les souffrances endurées. S'y trouvent également discutés de nombreux sujets concernant le droit maritime et surtout la dernière action menée par Fielding en tant que magistrat, profession que par la force des choses il vient de quitter. Quelques portraits croustillants de drôlerie et parfois non dénués de préjugés insulaires parsèment le récit, mais comme dans les romans, le pittoresque est absent des descriptions dont, à de rares exceptions près, le style reste calqué sur le langage poétique obligé de la fin du xviie et du début du xviiie siècle.\n" +
                        "\n" +
                        "L'ironie traverse l'ouvrage de page en page, dirigée contre certains personnages de rencontre, mais surtout contre le narrateur, plus parodique que franchement satirique, toujours comique. Elle s'appuie sur plusieurs récits de voyage anglais, mais essentiellement sur les épopées d'Homère et de Virgile dont les héros, à des degrés divers, représentent le passager souffrant balloté sur les flots en quête d'une nouvelle patrie.\n" +
                        "\n" +
                        "Le journal de cette traversée a été publié à titre posthume en janvier 1755, soit une année après la mort de l'auteur et ironiquement dix mois avant le tremblement de terre qui a incité Voltaire à se préoccuper de la divine Providence.";

        int extended = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt( i );

            if (c > 0x7F ) {
                ++extended;
            }

        }


        System.out.printf( "extended: %s vs full length: %s\n", extended, text.length() );


    }

    @Test
    @Ignore
    public void test2() throws Exception {

        int block_size = 5;

        char[] data = "abcdef".toCharArray();

        int blocks = (data.length / block_size) + 1;

        int offset = 0;

        // ** do all but the last block.
        for (int block = 0; block < blocks - 1; block++) {
            int length = block_size;

            System.out.printf( "Working on block: %s with offset %s and length %s\n", block, offset, length );
            offset += block_size;
        }

        // ** now to the last block
        {
            int block = blocks - 1;
            int length = data.length % block_size;
            System.out.printf( "Working on last block: %s with offset %s and length %s\n", block, offset, length );

        }


//
//        int block_offset = 0;
//
//        while( block_offset < data.length ) {
//
//
//
//            block_offset += block_size;
//
//        }


    }

    private String toString( ByteBuffer byteBuffer ) {
        return new String( byteBuffer.array(), byteBuffer.position(), byteBuffer.limit() );
    }

}
