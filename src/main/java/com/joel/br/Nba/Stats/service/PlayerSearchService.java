package com.joel.br.Nba.Stats.service;

import com.joel.br.Nba.Stats.model.PlayerInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class PlayerSearchService {



    private static final String URL_SEARCH = "https://www.espn.com/nba/players";


    @Cacheable(value = "playerInfoCache", key = "#playerName.toLowerCase()")
    public PlayerInfo findPlayerByName(String playerName) {


        try {
            Document searchDoc = Jsoup.connect(URL_SEARCH)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            //Buscar Todos os players
            Elements playerLinks = searchDoc.select("a.AnchorLink");

            String normalizedSearch = playerName.toLowerCase().replaceAll("\\s+", " ").trim();


            for(Element link : playerLinks) {
                String linkText = link.text().toLowerCase();

                if(linkText.contains(normalizedSearch)) {
                    String href = link.attr("href");

                    if(href.contains("/nba/player/_/id/")) {
                        String parts[] = href.split("/");

                        for(int i = 0; i < parts.length; i++) {
                            if(parts[i].equals("id") &&  i + 1 < parts.length) {
                                String playerId = parts[i + 1];

                                PlayerInfo playerInfo = new PlayerInfo();

                                playerInfo.setId(playerId);
                                playerInfo.setName(link.text());
                                playerInfo.setProfileUrl("https://www.espn.com" + href);


                                Element element = link.parent();
                                if (element != null) {
                                    Element positionElement = element.selectFirst("span.pl3");
                                    if (positionElement != null) {
                                        playerInfo.setPosition(positionElement.text());
                                    }
                                }

                                return playerInfo;

                            }
                        }
                    }
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;

    }


    public List<PlayerInfo> searchPlayers(String query) {
        List<PlayerInfo> results = new ArrayList<>();

        try {
            Document searchDoc = Jsoup.connect(URL_SEARCH)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            Elements playerLinks = searchDoc.select("a.AnchorLink");
            String normalizedQuery = query.toLowerCase().trim();

            for (Element link : playerLinks) {
                String linkText = link.text().toLowerCase();
                if (linkText.contains(normalizedQuery)) {
                    String href = link.attr("href");
                    if (href.contains("/nba/player/_/id/")) {
                        String[] parts = href.split("/");
                        for (int i = 0; i < parts.length; i++) {
                            if (parts[i].equals("id") && i + 1 < parts.length) {
                                PlayerInfo playerInfo = new PlayerInfo();
                                playerInfo.setId(parts[i + 1]);
                                playerInfo.setName(link.text());
                                playerInfo.setProfileUrl("https://www.espn.com" + href);

                                results.add(playerInfo);
                                break;
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error searching for players: " + e.getMessage());
        }

        return results;
    }



}





