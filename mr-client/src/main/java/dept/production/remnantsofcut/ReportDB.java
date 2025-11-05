/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.production.remnantsofcut;

import workDB.DB_new;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author user
 */
public class ReportDB extends DB_new {

    private String param_1 = "";
    private String param_2 = "";
    private String param_3 = "";
    private String param_4 = "";
    private String param_5 = "";
    private String param_6 = "";
    private String param_7 = "";
    private String param_8 = "";
    private String param_9 = "";
    private String param_10 = "";
    private String param_11 = "";
    private String param_12 = "";

    public void setQueryParams(int kod, int fas, String art, int size, int growth, int cw, boolean csize, boolean cgrowth, boolean ccw) {
        if (kod != 0) {
            param_1 = " AND kod_owner = ?";
        }
        if (fas != 0) {
            param_2 = " AND fas = ?";
        }

        if (!art.equals("")) {
            param_3 = " AND nar = ?";
        }

        if (size != 0) {
            param_4 = " AND rzm = ?";
        }

        if (growth != 0) {
            param_5 = " AND rst = ?";
        }

        if (cw != 0) {
            param_6 = " AND cw = ? ";
        }

        if (csize) {
            param_7 = ",0 AS rzm";
        } else {
            param_7 = ",rzm";
            param_8 = ",rzm";
        }

        if (cgrowth) {
            param_9 = ",0 AS rst";
        } else {
            param_9 = ",rst";
            param_10 = ",rst";
        }

        if (ccw) {
            param_11 = ",'' AS ncw";
        } else {
            param_11 = ",ncw";
            param_12 = ",ncw ";
        }
    }

    public List getColors() throws Exception {
        List<String> colors = new ArrayList<String>();
        String query = " SELECT ncw FROm nsi_cd ORDER BY ncw ";
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                colors.add(rs.getString(1).trim());
            }
        } catch (Exception e) {
            System.err.println("Ошибка getColors(): " + e);
            throw new Exception("Ошибка получения списка цветов ", e);
        }

        return colors;
    }

    public int getColorIndex(String color) throws Exception {
        int colorIndex = 0;
        String query = " SELECT cw FROM nsi_cd WHERE ncw = ? ";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, color);
            rs = ps.executeQuery();
            while (rs.next()) {
                colorIndex = rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getColorIndex(): " + e);
            throw new Exception("Ошибка получения индекса цвета ", e);
        }
        return colorIndex;
    }

    public String getBrigadeName(int nBrigade) throws Exception {
        String brNaim = "";
        String query = " SELECT naim FROM nsi_brig WHERE kod1 = ? ";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, nBrigade);
            rs = ps.executeQuery();
            while (rs.next()) {
                brNaim = rs.getString(1).trim();
            }
        } catch (Exception e) {
            System.err.println("Ошибка getBrigadeName(int nBrigade): " + e);
            throw new Exception("Ошибка txBrigade ", e);
        }
        return brNaim;
    }

    public Vector getModelsList(int brigade, int fas, String article, int size, int growth, int colorIndex) throws Exception {
        Vector models = new Vector();
        String query = " SELECT nsi_brig.kod1,nsi_brig.naim,fas,nar,ngpr" + param_7 + param_9 + param_11 + ",sum(v_marh_list1.kol-ISNULL(poshiv.kol_sdano,0)) AS ost " +
                " FROM v_marh_list1 " +
                " INNER JOIN marh_list " +
                " ON v_marh_list1.kod_marh = marh_list.kod " +
                " INNER JOIN nsi_brig " +
                " ON kod_owner = nsi_brig.kod1 " +
                " LEFT OUTER JOIN (SELECT TOP 100 PERCENT marh1_id,SUM(kol_sdano+sdacha) AS kol_sdano " +
                " FROM (SELECT marh1_id,kol_sdano,case when pdrto=888 then sdacha else 0 end as sdacha from poshiv) A " +
                " GROUP BY marh1_id ORDER BY marh1_id) poshiv " +
                " ON v_marh_list1.item_id=poshiv.marh1_id " +
                " WHERE kod_owner<500 AND kod_owner<>738 AND kod_owner<>138" + param_1 + param_2 + param_3 + param_4 + param_5 + param_6 +
                " GROUP BY nsi_brig.kod1,nsi_brig.naim,fas,nar,ngpr,rzm,rst,ncw " +
                " HAVING SUM(v_marh_list1.kol-ISNULL(poshiv.kol_sdano,0)) <> 0 " +
                " ORDER BY kod1,fas,nar" + param_8 + param_10 + param_12;

        try {
            ps = conn.prepareStatement(query);
            if (brigade != 0) {
                ps.setInt(1, brigade);
            }
            if (fas != 0) {
                if (brigade != 0) {
                    ps.setInt(2, fas);
                } else {
                    ps.setInt(1, fas);
                }
            }
            if (!article.equals("")) {
                if (fas != 0 & brigade != 0) {
                    ps.setString(3, article);
                } else {
                    if (fas != 0 | brigade != 0) {
                        ps.setString(2, article);
                    } else {
                        ps.setString(1, article);
                    }
                }

            }
            if (size != 0) {
                if (!article.equals("") & fas != 0 & brigade != 0) {
                    ps.setInt(4, size);
                } else {
                    if (!article.equals("") & fas != 0 & brigade == 0) {
                        ps.setInt(3, size);
                    } else {
                        if (!article.equals("") & fas == 0 & brigade != 0) {
                            ps.setInt(3, size);
                        } else {
                            if (article.equals("") & fas != 0 & brigade != 0) {
                                ps.setInt(3, size);
                            } else {
                                if (!article.equals("") & fas == 0 & brigade == 0) {
                                    ps.setInt(2, size);
                                } else {
                                    if (article.equals("") & fas == 0 & brigade != 0) {
                                        ps.setInt(2, size);
                                    } else {
                                        if (article.equals("") & fas != 0 & brigade == 0) {
                                            ps.setInt(2, size);
                                        } else {
                                            ps.setInt(1, size);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (growth != 0) {
                if (size != 0 & !article.equals("") & fas != 0 & brigade != 0) {
                    ps.setInt(5, growth);
                } else {
                    if (size != 0 & !article.equals("") & fas != 0 & brigade == 0) {
                        ps.setInt(4, growth);
                    } else {
                        if (size != 0 & !article.equals("") & fas == 0 & brigade != 0) {
                            ps.setInt(4, growth);
                        } else {
                            if (size != 0 & article.equals("") & fas != 0 & brigade != 0) {
                                ps.setInt(4, growth);
                            } else {
                                if (size == 0 & !article.equals("") & fas != 0 & brigade != 0) {
                                    ps.setInt(4, growth);
                                } else {
                                    if (size != 0 & !article.equals("") & fas == 0 & brigade == 0) {
                                        ps.setInt(3, growth);
                                    } else {
                                        if (size != 0 & article.equals("") & fas == 0 & brigade != 0) {
                                            ps.setInt(3, growth);
                                        } else {
                                            if (size == 0 & article.equals("") & fas != 0 & brigade != 0) {
                                                ps.setInt(3, growth);
                                            } else {
                                                if (size == 0 & !article.equals("") & fas != 0 & brigade == 0) {
                                                    ps.setInt(3, growth);
                                                } else {
                                                    if (size != 0 & article.equals("") & fas != 0 & brigade == 0) {
                                                        ps.setInt(3, growth);
                                                    } else {
                                                        if (size == 0 & !article.equals("") & fas == 0 & brigade != 0) {
                                                            ps.setInt(3, growth);
                                                        } else {
                                                            if (size != 0 & article.equals("") & fas == 0 & brigade == 0) {
                                                                ps.setInt(2, growth);
                                                            } else {
                                                                if (size == 0 & article.equals("") & fas == 0 & brigade != 0) {
                                                                    ps.setInt(2, growth);
                                                                } else {
                                                                    if (size == 0 & !article.equals("") & fas == 0 & brigade == 0) {
                                                                        ps.setInt(2, growth);
                                                                    } else {
                                                                        if (size == 0 & article.equals("") & fas != 0 & brigade == 0) {
                                                                            ps.setInt(2, growth);
                                                                        } else {
                                                                            ps.setInt(1, growth);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            if (colorIndex != 0) {
                if (growth != 0 & size != 0 & !article.equals("") & fas != 0 & brigade != 0) {
                    ps.setInt(6, colorIndex);
                } else {
                    if (growth != 0 & size != 0 & !article.equals("") & fas != 0 & brigade == 0) {
                        ps.setInt(5, colorIndex);
                    } else {
                        if (growth != 0 & size != 0 & !article.equals("") & fas == 0 & brigade != 0) {
                            ps.setInt(5, colorIndex);
                        } else {
                            if (growth != 0 & size != 0 & article.equals("") & fas != 0 & brigade != 0) {
                                ps.setInt(5, colorIndex);
                            } else {
                                if (growth != 0 & size == 0 & !article.equals("") & fas != 0 & brigade != 0) {
                                    ps.setInt(5, colorIndex);
                                } else {
                                    if (growth == 0 & size != 0 & !article.equals("") & fas != 0 & brigade != 0) {
                                        ps.setInt(5, colorIndex);
                                    } else {
                                        if (growth != 0 & size != 0 & !article.equals("") & fas == 0 & brigade == 0) {
                                            ps.setInt(4, colorIndex);
                                        } else {
                                            if (growth != 0 & size != 0 & article.equals("") & fas == 0 & brigade != 0) {
                                                ps.setInt(4, colorIndex);
                                            } else {
                                                if (growth != 0 & size == 0 & article.equals("") & fas != 0 & brigade != 0) {
                                                    ps.setInt(4, colorIndex);
                                                } else {
                                                    if (growth == 0 & size == 0 & !article.equals("") & fas != 0 & brigade != 0) {
                                                        ps.setInt(4, colorIndex);
                                                    } else {
                                                        if (growth != 0 & size != 0 & article.equals("") & fas != 0 & brigade == 0) {
                                                            ps.setInt(4, colorIndex);
                                                        } else {
                                                            if (growth != 0 & size == 0 & !article.equals("") & fas == 0 & brigade != 0) {
                                                                ps.setInt(4, colorIndex);
                                                            } else {
                                                                if (growth == 0 & size != 0 & article.equals("") & fas != 0 & brigade != 0) {
                                                                    ps.setInt(4, colorIndex);
                                                                } else {
                                                                    if (growth == 0 & size != 0 & !article.equals("") & fas != 0 & brigade == 0) {
                                                                        ps.setInt(4, colorIndex);
                                                                    } else {
                                                                        if (growth != 0 & size != 0 & article.equals("") & fas == 0 & brigade == 0) {
                                                                            ps.setInt(3, colorIndex);
                                                                        } else {
                                                                            if (growth != 0 & size == 0 & article.equals("") & fas == 0 & brigade != 0) {
                                                                                ps.setInt(3, colorIndex);
                                                                            } else {
                                                                                if (growth == 0 & size != 0 & article.equals("") & fas != 0 & brigade == 0) {
                                                                                    ps.setInt(3, colorIndex);
                                                                                } else {
                                                                                    if (growth == 0 & size == 0 & !article.equals("") & fas == 0 & brigade != 0) {
                                                                                        ps.setInt(3, colorIndex);
                                                                                    } else {
                                                                                        if (growth != 0 & size == 0 & !article.equals("") & fas == 0 & brigade == 0) {
                                                                                            ps.setInt(3, colorIndex);
                                                                                        } else {
                                                                                            if (growth == 0 & size != 0 & !article.equals("") & fas == 0 & brigade == 0) {
                                                                                                ps.setInt(3, colorIndex);
                                                                                            } else {
                                                                                                if (growth != 0 & size == 0 & article.equals("") & fas != 0 & brigade == 0) {
                                                                                                    ps.setInt(3, colorIndex);
                                                                                                } else {
                                                                                                    if (growth != 0 & size == 0 & article.equals("") & fas == 0 & brigade == 0) {
                                                                                                        ps.setInt(2, colorIndex);
                                                                                                    } else {
                                                                                                        if (growth == 0 & size == 0 & article.equals("") & fas == 0 & brigade != 0) {
                                                                                                            ps.setInt(2, colorIndex);
                                                                                                        } else {
                                                                                                            if (growth == 0 & size == 0 & !article.equals("") & fas == 0 & brigade == 0) {
                                                                                                                ps.setInt(2, colorIndex);
                                                                                                            } else {
                                                                                                                if (growth == 0 & size == 0 & article.equals("") & fas != 0 & brigade == 0) {
                                                                                                                    ps.setInt(2, colorIndex);
                                                                                                                } else {
                                                                                                                    if (growth == 0 & size != 0 & article.equals("") & fas == 0 & brigade == 0) {
                                                                                                                        ps.setInt(2, colorIndex);
                                                                                                                    } else {
                                                                                                                        ps.setInt(1, colorIndex);
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                Vector model = new Vector();
                model.add(rs.getInt(1));
                model.add(rs.getString(2).trim());
                model.add(rs.getInt(3));
                model.add(rs.getString(4).trim());
                model.add(rs.getString(5).trim());
                model.add(rs.getInt(6));
                model.add(rs.getInt(7));
                model.add(rs.getString(8).trim());
                model.add(rs.getInt(9));
                models.add(model);
            }
        } catch (Exception e) {
            System.err.println("Ошибка getModelsList() " + e);
            throw new Exception("Ошибка получения списка моделей ", e);
        }
        return models;
    }


}
