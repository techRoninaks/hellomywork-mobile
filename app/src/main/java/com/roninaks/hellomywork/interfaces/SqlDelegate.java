package com.roninaks.hellomywork.interfaces;


import com.roninaks.hellomywork.helpers.SqlHelper;

/**
 * Creates Response
 */

public interface SqlDelegate {
    void onResponse(SqlHelper sqlHelper);
}
