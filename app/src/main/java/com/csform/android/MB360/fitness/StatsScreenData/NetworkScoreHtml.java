package com.csform.android.MB360.fitness.StatsScreenData;

public class NetworkScoreHtml {

    private Integer urScore = 30;
    private Integer netWorkScore = 20;
    private Integer percentile10 = 0;
    private Integer percentile90 = 0;
    private Integer mnthScore = 0;
    private Integer Score = 0;

    String CSS = " style=\"position: absolute !important; right: 15px !important; \"";

    public String NetworkScore(Integer weekScore, Integer myScore,
                               Integer percentile10, Integer percentile90,
                               Integer mnthScore, Integer score) {

        int max_value = Math.max(Math.max(Math.max(Math.max(weekScore, myScore),
                Math.max(percentile10, percentile90)), mnthScore), score);

        int min_value = Math.min(Math.min(Math.min(Math.min(weekScore, myScore),
                Math.min(percentile10, percentile90)), mnthScore), score);

        this.urScore = weekScore == 0 ? 0 : Math.round(weekScore * 100 / max_value);
        this.netWorkScore = myScore == 0 ? 0 : Math.round(myScore * 100 / max_value);
        this.percentile10 = percentile10 == 0 ? 0 : Math.round(percentile10 * 100 / max_value);
        this.percentile90 = percentile90 == 0 ? 0 :Math.round(percentile90 * 100 / max_value);
        this.mnthScore = mnthScore == 0 ? 0 :Math.round(mnthScore * 100 / max_value);
        this.Score = score == 0 ? 0 : Math.round(score * 100 / max_value);

        return " <!doctype html>" +
                " <head>" +
                " <style>" +
                " .chart-head {" +
                "    color: #8492af;" +
                "    font-weight: bold;" +
                "    font-size: 18px;" +
                "}" +
                " .main_border {" +
                "    border-radius: 7px;" +
                "    max-height: 15px;" +
                "}" +
                ".first_sec {" +
                "    height: 12px;" +
                "    background-color: lightgray;" +
                "    border-bottom-left-radius: 7px;" +
                "    border-top-left-radius: 7px;" +
                "}" +
                ".divider {" +
                "    position: absolute;" +
                "    left: 0;" +
                "    border-left: 1px solid black;" +
                "    top: -5px;" +
                "    bottom: -5px;" +
                "    background: white;" +
                "}" +
                ".second_sec {" +
                "    height: 12px;" +
                "    background-color: #9f9e9e;" +
                "}" +
                ".third_sec {" +
                "    height: 12px;" +
                "    background-color: #5d5d5d;" +
                "}" +
                ".fourth_sec {" +
                "    height: 12px;" +
                "    background-color: #191414;" +
                "    border-bottom-right-radius: 7px;" +
                "    border-top-right-radius: 7px;" +
                "}" +
                ".set_position {" +
                "    position: absolute;" +
                "}" +
                ".border-left1 {" +
                "    border-left: 1px solid black;" +
                "    width: 5px !important;" +
                "}" +
                ".border-left1:hover {" +
                "    cursor: pointer;" +
                "}" +
                ".Chartcircle {" +
                "    display: inline-block;" +
                "    width: 7px;" +
                "    height: 7px;" +
                "    background: black;" +
                "    border-radius: 10px;" +
                "    position: absolute;" +
                "    left: -3px;" +
                "}" +
                ".Chartcircle {" +
                "    display: inline-block;" +
                "    width: 7px;" +
                "    height: 7px;" +
                "    background: black;" +
                "    border-radius: 10px;" +
                "    position: absolute;" +
                "    left: -3px;" +
                "}" +
                ".fs-10 {" +
                "    font-size: 10px;" +
                "    white-space: nowrap;" +
                "}" +
                "     .col-3 {" +
                "         width:25%!important;" +
                "         display:inline-block;" +
                "         float:left;" +
                "     }" +
                "     .float-right {" +
                "         float:right;" +
                "     }" +
                "h6.fs14{margin-bottom:0;margin-top: -8px; }" +
                ".fs18{font-size: 20px}"+
                ".ml-2{margin-left: .5rem!important;}" +
                ".margin_top_you{margin-top: 0px!important}" +
                " </style>" +
                " </head>" +
                " <body>" +
                " " +
                " <!--My network start-->" +
                "" +
                "        <div class=\"mt-2 myNetwork\">" +
                "            " +
                "            <div class=\"clearfix\">" +
                "" +
                "                <div class=\"tab-content bg-white\" id=\"nav-tabContentnetworkScore\">" +
                "                    <div class=\"tab-pane fade show active\" id=\"nav-networkScore\" role=\"tabpanel\" aria-labelledby=\"nav-weeklyScore-tab\">" +
                "                        <div class=\"row no-gutter  py-3 Graph\">" +
                "                            <div class=\"col-sm-4 pr\">" +
                "                                <div class=\"card card-side\" style=\"height: 277px;\">" +
                "                                    <div class=\"socialMain\" style=\"padding-bottom: 0px !important\">" +
                "                                        <div class=\"d-flex\">" +
                "                                            <div class=\"p-2 mr-auto\">" +
                "                                            </div>" +
                "                                        </div>" +
                "                                    </div>" +
                "                                    <div class=\"\">" +
                "" +
                "                                        <div class=\"container\" style=\"position: relative; padding: 0px;  margin-top: -10px;\">" +
                "                                            <span id=\"scoreminScore\">" + min_value + "</span>" +
                "                                            <span class=\"float-right \" id=\"scoreMaxbarWidth\">" + max_value + "</span>" +
                "                                            <div class=\"row main_border no-gutters\">" +
                "                                                <div class=\"col-3\">" +
                "                                                    <div class=\"first_sec\">" +
                "                                                    </div>" +
                "                                                </div>" +
                "                                                <div class=\"col-3\">" +
                "                                                    <div class=\"divider\">" +
                "                                                    </div>" +
                "                                                    <div class=\"second_sec\">" +
                "                                                    </div>" +
                "                                                </div>" +
                "                                                <div class=\"col-3\">" +
                "                                                    <div class=\"divider\">" +
                "                                                    </div>" +
                "                                                    <div class=\"third_sec\">" +
                "                                                    </div>" +
                "                                                </div>" +
                "                                                <div class=\"col-3\">" +
                "                                                    <div class=\"divider\">" +
                "                                                    </div>" +
                "                                                    <div class=\"fourth_sec\">" +
                "                                                    </div>" +
                "                                                </div>" +
                "                                            </div>" +
                "" +
                "                                            <div class=\"row\">" +
                "                                                <div class=\"set_position\" id=\"you\" style=\"left: " + this.Score + "%; z-index: 106\">" +
                "                                                    <div class=\"border-left1\" id=\"score_you\"  onclick=\"interface.callFromJS("+score+")\" "+
                "style=\"height: 20px; width: 5px\">" +
                "         <div class=\"hiddenScore\" style=\"position:absolute;top:-20px;left:-10px;font-weight:700\">"+score+"</div>" +
                "                                                    </div>" +
                "                                                    <span class=\"Chartcircle\"></span>" +
                "                                                    <div class=\"ml-2\" " + (this.Score > 80 ? CSS : "") + ">" +
                "                                                        <h6 class=\"fs18 fw600 margin_top_you\">You</h6>" +
                "                                                    </div>" +
                "                                                </div>" +
                "                                                <div class=\"set_position\" id=\"users_btm\" style=\"left: " + this.percentile10 + "%; z-index: 101\">" +
                "                                                    <div class=\"border-left1\" id=\"score_usersBtm\" onclick=\"interface.callFromJS("+percentile10+")\"" +
                "style=\"height: 80px; width: 1px\">" +
                "                                                    </div>" +
                "                                                    <span class=\"Chartcircle\"></span>" +
                "                                                    <div class=\"ml-2\"" + (this.percentile10 > 80 ? CSS : "") + ">" +
                "                                                        <h6 class=\"fs14 fw600\">Users </h6>" +
                "                                                        <span class=\"fs-10\">bottom 10%</span>" +
                "                                                    </div>" +
                "                                                </div>" +
                "                                                <div class=\"set_position\" id=\"you_weekago\" style=\"left: " + this.urScore + "%; z-index: 103\">" +
                "                                                    <div class=\"border-left1\" id=\"score_weekago\"  " +
                "style=\"height: 110px; width: 1px\">" +
                "                                                    </div>" +
                "                                                    <span class=\"Chartcircle\"></span>" +
                "                                                    <div class=\"ml-2\"" + (this.urScore > 80 ? CSS : "") + ">" +
                "                                                        <h6 class=\"fs14 fw600\">You </h6>" +
                "                                                        <span class=\"fs-10\">a week ago</span>" +
                "                                                    </div>" +
                "                                                </div>" +
                "                                                <div class=\"set_position\" id=\"you_monthago\" style=\"left: " + this.mnthScore + "%; z-index: 105\">" +
                "                                                    <div class=\"border-left1\" id=\"score_monthago\" onclick=\"interface.callFromJS("+mnthScore+")\"" +
                " style=\"height: 50px; width: 1px\">" +
                "                                                        <span class=\"hiddenScore\"></span>" +
                "                                                    </div>" +
                "                                                    <span class=\"Chartcircle\"></span>" +
                "                                                    <div class=\"ml-2\"" + (this.mnthScore > 80 ? CSS : "") + ">" +
                "                                                        <h6 class=\"fs14 fw600\">You </h6>" +
                "                                                        <span class=\"fs-10\">a month ago</span>" +
                "                                                    </div>" +
                "                                                </div>" +
                "                                                <div class=\"set_position\" id=\"users_top\" style=\"left: " + this.percentile90 + "%; z-index: 105\">" +
                "                                                    <div class=\"border-left1\" id=\"score_usersTop\" onclick=\"interface.callFromJS("+percentile10+")\" " +
                "style=\"height: 180px; width: 1px\">" +
                "                                                        <span class=\"hiddenScore\"></span>" +
                "" +
                "                                                    </div>" +
                "                                                    <span class=\"Chartcircle\"></span>" +
                "                                                    <div class=\"ml-2\"" + (this.percentile90 > 80 ? CSS : "") + ">" +
                "                                                        <h6 class=\"fs14 fw600\">Users </h6>" +
                "                                                        <span class=\"fs-10\">top 10%</span>" +
                "                                                    </div>" +
                "                                                </div>" +
                "" +
                "                                                <div class=\"set_position\" id=\"network\" style=\"left: " + this.netWorkScore + "%; z-index: 102\">" +
                "                                                    <div class=\"border-left1\" id=\"score_network\" onclick=\"interface.callFromJS("+myScore+")\" style=\"height: 140px; width: 1px\">" +
                "                                                        <span class=\"hiddenScore\"></span>" +
                "                                                    </div>" +
                "                                                    <span class=\"Chartcircle\"></span>" +
                "                                                    <div class=\"ml-2\"" + (this.netWorkScore > 80 ? CSS : "") + ">" +
                "                                                        <h6 class=\"fs14 fw600\">Your Network</h6>" +
                "                                                    </div>" +
                "                                                </div>" +
                "" +
                "                                            </div>" +
                "                                        </div>" +
                "" +
                "                                    </div>" +
                "                                </div>" +
                "                            </div>" +
                "" +
                "                            <!--Steps Graph-->" +


                "                        </div>" +
                "                    </div>" +


                "            </div>" +
                "" +
//                "<button onclick=\"interface.callFromJS()\">JavaScript interface</button>" +

                "        </div>" +
                "<script src=\"https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.4.1.min.js\"></script>" +

                "<script>" +
                "$(\".border-left1\").on('click',function(){" +
                        "alert('Workign ')"+
                "              $(this).append($(\"<span style='position:absolute;top:-20px;left:-5px;white-space:nowrap;word-break:keep-all;" +
                "font-weight:700;background-color:rgba(0,0,0,.8);color:white;padding:2px 5px;border-radius:5px' > \" + Math.round($(this).find(\".hiddenScore\").text()) + \"</span>\"));" +
                "          }, function () {" +
                "              $(this).find(\"span:last\").remove();" +
                "          }"+
                "</script>" +
                "</body>" +
                "</html>";
    }
}
