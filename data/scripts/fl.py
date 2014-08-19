#! /usr/bin/env python3
# -*- coding: utf-8 -*-

from math import pow, log2

def fl(d):
    return entropy(d)-collapsedentropy(d)

def collapsedentropy(d):
    sm = 0.0
    n = len(d)
    h = int(n/2)
    d2 = list(range(0,h))
    for i in range(0,h):
        d2[i] = d[i]+d[h+i]
        sm += 2*d2[i]*log2(d2[i])
    sm += log2(1)
    return -sm/2



def entropy(d):
    sm = 0.0
    n = len(d)
    h = int(n/2)
    # labels
    sm += sum(d[0:h])*log2(sum(d[0:h]))
    sm += sum(d[h:n])*log2(sum(d[h:n]))
    # contexts
    for i in range(0,h):
        p = d[i]+d[h+i]
        sm += p*log2(p)
    # words
    for i in range(0,n):
        sm += d[i]*log2(d[i])
    return -sm/2



def getCrossZipf(n,s,q):
    out = list(range(0,n))
    sm = 0.0
    for i in range(1,int((n+2)/2)):
        out[i-1] = 1.0/pow(i,s)
        out[n-i] = out[i-1]*q
    for i in range(0,n):
        sm += out[i]
    for i in range(0,n):
        out[i] /= sm
    return out

def getDualZipf(n,s,q):
    out = list(range(0,n))
    h = int(n/2)
    sm = 0.0
    for i in range(1,int((n+2)/2)):
        out[i-1] = 1.0/pow(i,s)
        out[h+i-1] = out[i-1]*q
    for i in range(0,n):
        sm += out[i]
    for i in range(0,n):
        out[i] /= sm
    return out
